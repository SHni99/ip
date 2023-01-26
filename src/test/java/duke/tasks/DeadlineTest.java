package duke.tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeadlineTest {
    @Test
    public void deadlineTest () {
        assertEquals("[D][] bring books(by: 9 AUGUST 2022, 8pm )",
                new Deadline("bring books", "2022-08-09 8pm").toString());
    }
}