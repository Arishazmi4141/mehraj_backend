package e_commerce.khilat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);

}
