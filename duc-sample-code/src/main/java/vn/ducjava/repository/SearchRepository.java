package vn.ducjava.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.ducjava.dto.response.PageResponse;
import vn.ducjava.repository.criteria.SearchCriteria;
import vn.ducjava.model.User;
import vn.ducjava.repository.criteria.UserSearchCriteriaQueryConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getAllUsersWithSearch(int pageNo, int pageSize, String sortBy, Sort.Direction direction, String search) {
        StringBuilder sqlQuery = new StringBuilder("select new vn.ducjava.dto.response.UserDetailResponse(u.id id, u.firstName firstName, u.lastName lastName, u.email email, u.phone phone) from User u where 1=1");
        if(StringUtils.hasLength(search)) {
            sqlQuery.append("and lower(u.firstName) like lower(:firstName)");
            sqlQuery.append("or lower(u.lastName) like lower(:lastName)");
            sqlQuery.append("or lower(u.email) like lower(:email)");
        }

        if(StringUtils.hasLength(sortBy)) {
            sqlQuery.append("order by u." + sortBy + " " + direction.name());
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(pageNo);
        selectQuery.setMaxResults(pageSize);
        if(StringUtils.hasLength(search)){
            selectQuery.setParameter("firstName", "%" + search +"%");
            selectQuery.setParameter("lastName", "%" + search +"%");
            selectQuery.setParameter("email", "%" + search +"%");
        }

        List users = selectQuery.getResultList();

        System.out.println(users);
        // query ra list user
        // ///////////
        StringBuilder sqlCountQuery = new StringBuilder("select count(*) from User u where 1=1");
        if(StringUtils.hasLength(search)){
            sqlCountQuery.append("and lower(u.firstName) like lower(:firstName)");
            sqlCountQuery.append("or lower(u.lastName) like lower(:lastName)");
            sqlCountQuery.append("or lower(u.email) like lower(:email)");
        }

        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if(StringUtils.hasLength(search)){
            selectCountQuery.setParameter("firstName", "%" + search +"%");
            selectCountQuery.setParameter("lastName", "%" + search +"%");
            selectCountQuery.setParameter("email", "%" + search +"%");
        }
        Long totalElements = (Long) selectCountQuery.getSingleResult();
        System.out.println(totalElements);
        // query ra so record

        Page<?> page = new PageImpl<Objects>(users, PageRequest.of(pageNo, pageSize), totalElements);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .items(page.stream().toList())
                .build();

    }

    public PageResponse advanceSearchUser(int pageNo, int pageSize, String sortBy, String... search)  {

        //1. lấy ra danh sách user // firstName:bui ai duc,id:23
        List<SearchCriteria> criteriaList = new ArrayList<>();
        if (search != null) {
            for (String s : search){
                Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        //2. lấy ra số lượng bản ghi

        List<User> users = getUsers(pageNo, pageSize, criteriaList, sortBy);

            return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(0)
                .items(users)
                .build();
    }

    private List<User> getUsers(int pageNo, int pageSize, List<SearchCriteria> criteriaList, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        // Xử lý các điều kiện tìm kiếm

        Predicate predicate = criteriaBuilder.conjunction(); // where 1=1
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);

        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        query.where(predicate);

        //sort
        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(columnName)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(columnName)));
                }
            }
        }


        return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();


    }

}
