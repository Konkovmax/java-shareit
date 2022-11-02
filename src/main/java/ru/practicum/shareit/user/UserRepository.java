
    package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;

    //@RepositoryRestResource(path = "people")
    public interface UserRepository extends JpaRepository<User, Integer>{

//    @RestResource(path = "emails")
//    List<User> findByEmailContainingIgnoreCase(@Param("email") String emailSearch);
//
//    List<UserShort> findAllByEmailContainingIgnoreCase(String emailSearch);

}
