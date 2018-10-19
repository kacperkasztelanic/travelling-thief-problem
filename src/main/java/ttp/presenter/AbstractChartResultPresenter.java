package ttp.presenter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractChartResultPresenter implements ResultPresenter {

    protected static final String TITLE = "Travelling Thief Problem";
    protected static final String X_LABEL = "Generation";
    protected static final String Y_LABEL = "Value";
    
    protected static final String MIN_SERIES_LABEL = "minValue";
    protected static final String MAX_SERIES_LABEL = "maxValue";
    protected static final String AVG_SERIES_LABEL = "avgValue";

    protected final String file;
    protected final int width;
    protected final int height;
}
