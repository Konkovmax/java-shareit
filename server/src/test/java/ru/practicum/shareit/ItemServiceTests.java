package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTests {


//    @Autowired
//    private TestEntityManager em;
//
//    @Test
//    void searchTest() {
//        Item item1 = new Item();
//        item1.setName("Name");
//        item1.setDescription("Bosch");
//        item1.setAvailable(true);
//        String text = "Bosch";
//        em.persist(item1);
//        TypedQuery<Item> query = em.getEntityManager()
//                .createQuery(" select i from Item i " +
//                        "where (upper(i.name) like upper(concat('%', :text, '%')) " +
//                        " or upper(i.description) like upper(concat('%', :text, '%')))" +
//                        "and i.available is true ", Item.class);
//        Item item = query.setParameter("text", text).getSingleResult();
//        Assertions.assertEquals(item, item1);
//    }
}
