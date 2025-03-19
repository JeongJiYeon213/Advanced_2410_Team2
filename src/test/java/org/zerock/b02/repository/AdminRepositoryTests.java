package org.zerock.b02.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b02.domain.Admin;

import java.util.List;
import java.util.Random;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Log4j2
public class AdminRepositoryTests {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    public void testAdmin() {
        List<String> words = List.of("Steel", "Metal", "Iron", "Forge", "Titan", "Bolt", "Alloy", "Weld", "Smith",
                "Alpha", "Beta", "Gamma", "Delta", "Echo", "Falcon", "Griffin", "Hawk", "Jaguar", "Knight");
        Random random = new Random();

        IntStream.rangeClosed(1, 110).forEach(i -> {
            String randomName = words.get(random.nextInt(words.size())) + words.get(random.nextInt(words.size())); // 두 단어 조합
            long randomId = 10000000L + random.nextInt(90000000); // 8자리 숫자 생성
            int randomPassword = 1000 + random.nextInt(9000); // 4자리 숫자 생성
            String position = (i == 1) ? "사장" : "사원"; // 첫 번째만 "사장", 나머지는 "사원"

            String phoneNum = "101" + "-"+ (1000 + random.nextInt(9000)) + "-" + (1000 + random.nextInt(9000));


            Admin admin = Admin.builder()
                    .adminName(randomName)
                    .adminId(randomId)
                    .adminPassword(randomPassword)
                    .position(position)
                    .phoneNum(phoneNum) // 랜덤 전화번호 추가
                    .build();

            Admin result = adminRepository.save(admin);
            log.info("BNO: " + result.getBno() + ", NAME: " + randomName + ", ID: " + randomId +
                    ", PASSWORD: " + randomPassword + ", POSITION: " + position + ", PHONE: " + phoneNum);
        });
    }

    @Test
    public void testSelectAdminById() {
        Long targetId = adminRepository.findAll().get(5).getBno(); // 첫 번째 Admin의 ID 가져오기

        Optional<Admin> admin = adminRepository.findById(targetId);
        assertThat(admin).isPresent(); // 데이터가 존재하는지 확인

        log.info("Found Admin: " + admin.get().getAdminName() + ", ID: " + admin.get().getBno());
    }

    @Test
    public void testDeleteAdminById() {
        Long targetId = adminRepository.findAll().get(0).getBno(); // 첫 번째 Admin의 ID 가져오기
        adminRepository.deleteById(targetId);

        Optional<Admin> deletedAdmin = adminRepository.findById(targetId);
        assertThat(deletedAdmin).isEmpty(); // 삭제가 되었는지 확인
        log.info("Admin with ID " + targetId + " deleted.");
    }

}
