package baeldungstreams;

import com.codepoetics.protonpack.Indexed;
import com.codepoetics.protonpack.StreamUtils;
import com.codepoetics.protonpack.Streamable;
import com.codepoetics.protonpack.collectors.CollectorUtils;
import com.codepoetics.protonpack.selectors.Selectors;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codepoetics.protonpack.StreamUtils.*;
import static java.util.Arrays.stream;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {

        assertTrue( true );
    }

    @Test
    public void testTakeWhile() {
        Stream<Integer> streamOfInt = Stream
                .iterate(1, i -> i + 1);
        List<Integer> result = takeWhile(streamOfInt, i -> i < 5)
                .collect(Collectors.toList());
        System.out.println(result);
    }

    @Test
    public void testTakeUtil() {
        Stream<Integer> streamOfInt = Stream
                .iterate(1, i -> i + 1);
        List<Integer> result = takeUntil(streamOfInt, i -> i >= 5)
                .collect(Collectors.toList());
        System.out.println(result);
    }

    @Test
    public void testZip() {
        String[] clubs = {"Juventus", "Barcelona", "Liverpool", "PSG"};
        String[] players = {"Ronaldo", "Messi", "Salah"};
        Set<String> zippedFrom2Sources = zip(stream(clubs), stream(players), (club, player) -> club + " " + player)
                .collect(Collectors.toSet());
        Iterator itr = zippedFrom2Sources.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }

    @Test
    public void testZipWithIndex() {
        Stream<String> streamOfClubs = Stream
                .of("Juventus", "Barcelona", "Liverpool");
        Set<Indexed<String>> zipsWithIndex = zipWithIndex(streamOfClubs)
                .collect(Collectors.toSet());
        System.out.println(zipsWithIndex);
    }

    @Test
    public void testMerge() {
        Stream<String> streamOfClubs = Stream
                .of("Juventus", "Barcelona", "Liverpool", "PSG");
        Stream<String> streamOfPlayers = Stream
                .of("Ronaldo", "Messi", "Salah");
        Stream<String> streamOfLeagues = Stream
                .of("Serie A", "La Liga", "Premier League");

        Set<String> merged = merge(
                () ->  "",
                (valOne, valTwo) -> valOne + " " + valTwo,
                streamOfClubs,
                streamOfPlayers,
                streamOfLeagues)
                .collect(Collectors.toSet());
        System.out.println(merged);
    }

    @Test
    public void testMergeToList() {
        Stream<String> streamOfClubs = Stream
                .of("Juventus", "Barcelona", "PSG");
        Stream<String> streamOfPlayers = Stream
                .of("Ronaldo", "Messi");

        Stream<List<String>> mergedStreamOfList = mergeToList(streamOfClubs, streamOfPlayers);
        List<List<String>> mergedListOfList = mergedStreamOfList
                .collect(Collectors.toList());
        for (List<String> list : mergedListOfList) {
            System.out.println(list);
        }

    }

    @Test
    public void testInterLeave() {
        Stream<String> streamOfClubs = Stream
                .of("Juventus", "Barcelona", "Liverpool");
        Stream<String> streamOfPlayers = Stream
                .of("Ronaldo", "Messi");
        Stream<String> streamOfLeagues = Stream
                .of("Serie A", "La Liga");

        List<String> interleavedList = interleave(Selectors.takeMax(), streamOfClubs, streamOfPlayers, streamOfLeagues)
                .collect(Collectors.toList());
        System.out.println(interleavedList.size());
        System.out.println(interleavedList);
    }

    @Test
    public void testSkipUntil() {
        Integer[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        List skippedUntilGreaterThan5 = StreamUtils
                .skipUntil(stream(numbers), i -> i > 5)
                .collect(Collectors.toList());
        System.out.println(skippedUntilGreaterThan5);
    }

    @Test
    public void testSkipWhile() {
        Integer[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        List skippedWhileGreaterThan5 = StreamUtils
                .skipWhile(stream(numbers), i -> i > 5)
                .collect(Collectors.toList());
        System.out.println(skippedWhileGreaterThan5);
    }

    @Test
    public void testUnfold() {
        Stream<Integer> unfolded = StreamUtils
                .unfold(2, i -> (i < 100)
                        ? Optional.of(i * i) : Optional.empty());
        System.out.println(unfolded.collect(Collectors.toList()));
    }

    @Test
    public void testWindowed() {
        Integer[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8 };

        List<List> windowedWithSkip1 = StreamUtils
                .windowed(stream(numbers), 3, 1)
                .collect(Collectors.toList());
        System.out.println(windowedWithSkip1);
    }

    @Test
    public void testAggregate() {
        Integer[] numbers = { 1, 2, 2, 3, 4, 4, 4, 5 };
        List<List> aggregated = StreamUtils
                .aggregate(Arrays.stream(numbers), (int1, int2) -> {
                    return int1.compareTo(int2) == 0;
                })
                .collect(Collectors.toList());
        System.out.println(aggregated);
    }

    @Test
    public void testAggregateFixedSize() {
        Integer[] numbers = { 1, 2, 2, 3, 4, 4, 4, 5, 5 };
        List<List> aggregatedFixSize = StreamUtils
                .aggregate(stream(numbers), 5)
                .collect(Collectors.toList());
        System.out.println(aggregatedFixSize);
    }

    @Test
    public void testAggregateOnListCondition() {
        Integer[] numbers = { 1, 1, 2, 3, 4, 4, 5 };
        Stream<List<Integer>> aggregated = StreamUtils
                .aggregateOnListCondition(stream(numbers),
                        (currentList, nextInt) -> currentList.stream().mapToInt(Integer::intValue).sum() + nextInt <= 5);
        Object[] obj = aggregated.toArray();
        for (Object o : obj) {
            System.out.println(o);
        }
    }

    @Test
    public void testStreamable() {
        Streamable<String> s = Streamable.of("a", "b", "c", "d");
        List<String> collected1 = s.collect(Collectors.toList());
        List<String> collected2 = s.collect(Collectors.toList());
        System.out.println(collected1.size());
        System.out.println(collected2.size());
        System.out.println(collected1.get(0));
        System.out.println(collected2.get(1));
    }

    @Test
    public void testStreamableNumbers() {
        Streamable<Integer> i = Streamable.of(1, 2, 3, 4);
        List<Integer> collected1 = i.collect(Collectors.toList());
        List<Integer> collected2 = i.collect(Collectors.toList());
        for (Integer integer : collected1) {
            System.out.println(integer);
        }
        for (Integer integer : collected2) {
            System.out.println(integer);
        }
    }

    @Test
    public void testMaxBy() {
        Stream<String> clubs = Stream.of("Juventus", "Barcelona", "PSG");
        Optional<String> longestName = clubs.collect(CollectorUtils.maxBy(String::length));
        longestName.ifPresent(System.out::println);
    }

    @Test
    public void testMinBy() {
        Stream<String> clubs = Stream.of("Juventus", "Barcelona", "PSG");
        Optional<String> longestName = clubs.collect(CollectorUtils.minBy(String::length));
        longestName.ifPresent(System.out::println);
    }

    @Test
    public void testUnique() {
        Stream<Integer> singleElement = Stream.of(1);
        Optional<Integer> unique = singleElement.collect(CollectorUtils.unique());
        unique.ifPresent(System.out::println);
    }
}
