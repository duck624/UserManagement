package vn.ducjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducjava.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
