package org.springframework.samples.gitvision.commit;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.gitvision.commit.CommitRepository;
import org.springframework.samples.gitvision.commit.model.commitsByPerson.CommitsByPerson;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.CommitsByTimePeriod;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.TimePeriod;

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
        List<Object[]> query = this.commitRepository.getNumCommitsByUser(ID_REPOSITORY);
        CommitsByPerson commitsByPerson = CommitsByPerson.of(query);
        Map<String, Long> count = commitsByPerson.parseNumCommitsByUsername();
        assertEquals(6, count.get(USERNAME_1).intValue());
        assertEquals(5, count.get(USERNAME_2).intValue());
        assertEquals(1, count.get(USERNAME_3).intValue());
    }

    @Test
    void testGetNumCommitsByUserBeforeThat() {
        // <--  2004-02-01 23:00:00 
        LocalDateTime endDateTime = LocalDateTime.of(2004, 2, 1, 23, 0, 0);
        List<Object[]> query = this.commitRepository.getNumCommitsByUserBeforeThat(ID_REPOSITORY, endDateTime);
        CommitsByPerson commitsByPerson = CommitsByPerson.of(query);
        Map<String, Long> count = commitsByPerson.parseNumCommitsByUsername();
        assertEquals(6, count.get(USERNAME_1).intValue());
        assertEquals(4, count.get(USERNAME_2).intValue());
        assertEquals(0, count.get(USERNAME_3).intValue());
    }

    @Test
    void testGetNumCommitsByUserAfterThat() {
        // 2003-02-01 23:00:00  -->
        LocalDateTime starDateTime = LocalDateTime.of(2003, 2, 1, 23, 0, 0);
        List<Object[]> query = this.commitRepository.getNumCommitsByUserAfterThat(ID_REPOSITORY, starDateTime);
        CommitsByPerson commitsByPerson = CommitsByPerson.of(query);
        Map<String, Long> count = commitsByPerson.parseNumCommitsByUsername();
        assertEquals(count.get(USERNAME_1).intValue(), 2);
        assertEquals(count.get(USERNAME_2).intValue(), 3);
        assertEquals(count.get(USERNAME_3).intValue(), 1);
    }

    @Test
    void testGetNumCommitsByUserOnDate() {
        // 2003-02-01 23:00:00  --  2004-02-01 23:00:00
        LocalDateTime starDateTime = LocalDateTime.of(2003, 2, 1, 23, 0, 0),
                endDateTime = LocalDateTime.of(2004, 2, 1, 23, 0, 0);
        List<Object[]> query = this.commitRepository.getNumCommitsByUserOnDate(ID_REPOSITORY, starDateTime, endDateTime);
        CommitsByPerson commitsByPerson = CommitsByPerson.of(query);
        Map<String, Long> count = commitsByPerson.parseNumCommitsByUsername();
        assertEquals(2, count.get(USERNAME_1).intValue());
        assertEquals(2, count.get(USERNAME_2).intValue());
        assertEquals(0, count.get(USERNAME_3).intValue());
    }

    @ParameterizedTest
    @CsvSource({"16,7", "17,1", "18,1", "19,2", "23,1"})
    void testGetNumCommitsByHour(Integer hour, Integer cont) {
        List<Integer[]> ls = commitRepository.getNumCommitsByHour(ID_REPOSITORY);
        CommitsByTimePeriod commitsByTimePeriod = CommitsByTimePeriod.of(ls, TimePeriod.HOUR);
        assertEquals(cont, commitsByTimePeriod.get(hour));
    }

    @ParameterizedTest
    @CsvSource({"1,1", "2,0", "3,4", "4,1", "5,1", "6,2", "7,3"})
    void testGetNumCommitsByDayOfWeek(Integer dayOfWeek, Integer cont) {
        List<Integer[]> ls = commitRepository.getNumCommitsByDayOfWeek(ID_REPOSITORY);
        CommitsByTimePeriod commitsByTimePeriod = CommitsByTimePeriod.of(ls, TimePeriod.DAY_OF_WEEK);
        assertEquals(cont, commitsByTimePeriod.get(dayOfWeek));
    }

    @ParameterizedTest
    @CsvSource({"1,7", "2,3", "3,2"})
    void testGetNumCommitsByMonth(Integer month, Integer cont) {
        List<Integer[]> ls = commitRepository.getNumCommitsByMonth(ID_REPOSITORY);
        CommitsByTimePeriod commitsByTimePeriod = CommitsByTimePeriod.of(ls, TimePeriod.MONTH);
        assertEquals(cont, commitsByTimePeriod.get(month));
    }

    @ParameterizedTest
    @CsvSource({"2003,9", "2004,3"})
    void testGetNumCommitsByYear(Integer year, Integer cont) {
        List<Integer[]> ls = commitRepository.getNumCommitsByYear(ID_REPOSITORY);
        CommitsByTimePeriod commitsByTimePeriod = CommitsByTimePeriod.of(ls, TimePeriod.YEAR);
        assertEquals(cont, commitsByTimePeriod.get(year));
    }


}
