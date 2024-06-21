package org.springframework.samples.gitVision.commit;

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

    @Test
    void testGetNumCommitsByUser() {
        List<CommitsByPerson> query = this.commitRepository.getNumCommitsByUser();
        Map<String, Long> count = CommitsByPerson.parseNumCommitsByUsername(query);
        assertEquals(6, count.get("user_1").intValue());
        assertEquals(5, count.get("user_2").intValue());
        assertEquals(1, count.get("user_3").intValue());
    }

    @Test
    void testGetNumCommitsByUserBeforeThat() {
        // <--  2004-02-01 23:00:00 
        LocalDateTime endDateTime = LocalDateTime.of(2004, 2, 1, 23, 0, 0);
        List<CommitsByPerson> query = this.commitRepository.getNumCommitsByUserBeforeThat(endDateTime);
        Map<String, Long> count = CommitsByPerson.parseNumCommitsByUsername(query);
        assertEquals(6, count.get("user_1").intValue());
        assertEquals(4, count.get("user_2").intValue());
        assertEquals(0, count.get("user_3").intValue());
    }

    @Test
    void testGetNumCommitsByUserAfterThat() {
        // 2003-02-01 23:00:00  -->
        LocalDateTime starDateTime = LocalDateTime.of(2003, 2, 1, 23, 0, 0);
        List<CommitsByPerson> query = this.commitRepository.getNumCommitsByUserAfterThat(starDateTime);
        Map<String, Long> count = CommitsByPerson.parseNumCommitsByUsername(query);
        assertEquals(count.get("user_1").intValue(), 2);
        assertEquals(count.get("user_2").intValue(), 3);
        assertEquals(count.get("user_3").intValue(), 1);
    }

    @Test
    void testGetNumCommitsByUserOnDate() {
        // 2003-02-01 23:00:00  --  2004-02-01 23:00:00
        LocalDateTime starDateTime = LocalDateTime.of(2003, 2, 1, 23, 0, 0),
                endDateTime = LocalDateTime.of(2004, 2, 1, 23, 0, 0);
        List<CommitsByPerson> query = this.commitRepository.getNumCommitsByUserOnDate(starDateTime, endDateTime);
        Map<String, Long> count = CommitsByPerson.parseNumCommitsByUsername(query);
        assertEquals(2, count.get("user_1").intValue());
        assertEquals(2, count.get("user_2").intValue());
        assertEquals(0, count.get("user_3").intValue());
    }


}
