package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.TypedQuery;

//@SpringBootTest
@DataJpaTest
//    @RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
        // @Import(JpaConfig.class)

class ItemServiceTests {


    @Autowired
    private TestEntityManager em;

//        private final ItemServiceImpl itemService;

//        @Autowired
//        private ItemRepository repository;


//        @Test
//        public void contextLoads() {
//            Assertions.assertNotNull(em);
//        }

    @Test
    void searchTest() {
        // List<Item> items = Arrays.asList(
        Item item1 = new Item();
        item1.setName("Name");
        item1.setDescription("Bosch");
        item1.setAvailable(true);
//                Item item2 = new Item(2, "Drill Bosch", "Professional", true, null, null);
        String text = "Bosch";


//        when(mockRepository.search(query, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(items));
//
//        mockMvc.perform(get("/items/search")
//                                .param("text", query)
////                        .header("X-Sharer-User-Id",1)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].name", is("Drill Bosch")))
//                .andExpect(jsonPath("$[0].description", is("Professional")))
//                .andExpect(jsonPath("$[0].available", is(true)))
////                  .andExpect(jsonPath("$[1].id", is(2)))
////                .andExpect(jsonPath("$[1].name", is("2Name")))
////                .andExpect(jsonPath("$[1].description", is("2Description")))
////                .andExpect(jsonPath("$[1].available", is(true)));
//        ;
//        verify(mockRepository, times(1)).search(query, PageRequest.of(0, 10));
        //            Assertions.assertNull(emp.getEmployeeId());
        // repository.save(item1);

        em.persist(item1);
//            em.persist(item2);

        TypedQuery<Item> query = em.getEntityManager()
                .createQuery(" select i from Item i " +
                        "where (upper(i.name) like upper(concat('%', :text, '%')) " +
                        " or upper(i.description) like upper(concat('%', :text, '%')))" +
                        "and i.available is true ", Item.class);
        Item item = query.setParameter("text", text).getSingleResult();
//            List<ItemDto> results = itemService.search(text, 0,10);
        // List<Item> results = repository.search(text, PageRequest.of(0,10)).toList();
        Assertions.assertEquals(item, item1);
    }
}
