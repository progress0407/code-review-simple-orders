package codereview.simpleorder.presentation;

import codereview.simpleorder.domain.item.Clothes;
import codereview.simpleorder.dto.item.ClothesResponse;
import codereview.simpleorder.dto.item.ClothesResponses;
import codereview.simpleorder.repository.command.ClothesRepository;
import codereview.simpleorder.repository.query.ClothesQueryRepository;
import codereview.simpleorder.support.AbstractAcceptanceTest;
import codereview.simpleorder.support.JsonFileConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

class ClothesQueryAcceptanceTest extends AbstractAcceptanceTest {

    @Autowired
    private ClothesQueryRepository queryRepository;

    @Autowired
    private ClothesRepository repository;

    @Test
    void clothes_조회는_리스트를_반환한다() {
        // given
        List<Clothes> initData = JsonFileConverter.fromJsonFile("/clothes-responses.json", Clothes.class);
        repository.saveAll(initData);

        // when
        ClothesResponses 조회한_리스트 = get("/items").as(ClothesResponses.class);
        ClothesResponse 양털_스웨터 = findOneBy(조회한_리스트, "양털 스웨터");
        ClothesResponse 기모_블랙진 = findOneBy(조회한_리스트, "기모 블랙진");

        // then
        assertAll(
                assertNotNull(조회한_리스트),
                assertNotNull(양털_스웨터.getId()),
                assertEquality(양털_스웨터.getSize(), "100L"),
                assertEquality(양털_스웨터.getPrice(), 20_000),
                assertNotNull(기모_블랙진.getId()),
                assertEquality(기모_블랙진.getSize(), "95M"),
                assertEquality(기모_블랙진.getPrice(), 55000)
        );
    }


    private static ClothesResponse findOneBy(ClothesResponses clothesResponses, String name) {
        return clothesResponses.getClothes().stream()
                .filter(clothesResponse -> clothesResponse.getName().equals(name))
                .findAny()
                .get();
    }
}
