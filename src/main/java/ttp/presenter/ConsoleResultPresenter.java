package ttp.presenter;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ttp.model.Statistics;

@AllArgsConstructor(staticName = "instance")
@EqualsAndHashCode
@ToString
public class ConsoleResultPresenter implements ResultPresenter {

    private final PrintWriter pw;

    @Override
    public void present(List<Statistics> results) {
        String result = results.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
        pw.println(result);
    }
}
