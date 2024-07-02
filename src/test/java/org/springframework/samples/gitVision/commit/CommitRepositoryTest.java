package org.springframework.samples.gitvision.commit;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.gitvision.commit.CommitRepository;
import org.springframework.samples.gitvision.commit.model.CommitsByPerson;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommitRepositoryTest {

    @Autowired
    CommitRepository commitRepository;

    private static Long ID_REPOSITORY = 1L;
    private static String USERNAME_1 = "username_1";
    private static String USERNAME_2 = "username_2";
    private static String USERNAME_3 = "username_3";

    @Test
    void testGetNumCommitsByUser() {
        List<CommitsByPerson> query = this.commitRepository.getNumCommitsByUser(ID_REPOSITORY);
        Map<String, Long> count = CommitsByPerson.parseNumCommitsByUsername(query);
        assertEquals(6, count.get(USERNAME_1).intValue());
        assertEquals(5, count.get(USERNAME_2).intValue());
        assertEquals(1, count.get(USERNAME_3).intValue());
    }

    @Test
    void testGetNumCommitsByUserBeforeThat() {
        // <--  2004-02-01 23:00:00 
        LocalDateTime endDateTime = LocalDateTime.of(2004, 2, 1, 23, 0, 0);
        List<CommitsByPerson> query = this.commitRepository.getNumCommitsByUserBeforeThat(ID_REPOSITORY, endDateTime);
        Map<String, Long> count = CommitsByPerson.parseNumCommitsByUsername(query);
        assertEquals(6, count.get(USERNAME_1).intValue());
        assertEquals(4, count.get(USERNAME_2).intValue());
        assertEquals(0, count.get(USERNAME_3).intValue());
    }

    @Test
    void testGetNumCommitsByUserAfterThat() {
        // 2003-02-01 23:00:00  -->
        LocalDateTime starDateTime = LocalDateTime.of(2003, 2, 1, 23, 0, 0);
        List<CommitsByPerson> query = this.commitRepository.getNumCommitsByUserAfterThat(ID_REPOSITORY, starDateTime);
        Map<String, Long> count = CommitsByPerson.parseNumCommitsByUsername(query);
        assertEquals(count.get(USERNAME_1).intValue(), 2);
        assertEquals(count.get(USERNAME_2).intValue(), 3);
        assertEquals(count.get(USERNAME_3).intValue(), 1);
    }

    @Test
    void testGetNumCommitsByUserOnDate() {
        // 2003-02-01 23:00:00  --  2004-02-01 23:00:00
        LocalDateTime starDateTime = LocalDateTime.of(2003, 2, 1, 23, 0, 0),
                endDateTime = LocalDateTime.of(2004, 2, 1, 23, 0, 0);
        List<CommitsByPerson> query = this.commitRepository.getNumCommitsByUserOnDate(ID_REPOSITORY, starDateTime, endDateTime);
        Map<String, Long> count = CommitsByPerson.parseNumCommitsByUsername(query);
        assertEquals(2, count.get(USERNAME_1).intValue());
        assertEquals(2, count.get(USERNAME_2).intValue());
        assertEquals(0, count.get(USERNAME_3).intValue());
    }

    @Test
    void testGetNumCommitsByHour() {
        List<Object[]> res = commitRepository.getNumCommitsByHour(ID_REPOSITORY);
    }

    @Test
    void testGetNumCommitsByDayOfWeek() {
        List<Object[]> res = commitRepository.getNumCommitsByDayOfWeek(ID_REPOSITORY);
    }

    @Test
    void testGetNumCommitsByMonth() {
        List<Object[]> res = commitRepository.getNumCommitsByMonth(ID_REPOSITORY);
    }

    @Test
    void testGetNumCommitsByUser2() {
        List<Object[]> res = commitRepository.getNumCommitsByYear(ID_REPOSITORY);
    }


}
