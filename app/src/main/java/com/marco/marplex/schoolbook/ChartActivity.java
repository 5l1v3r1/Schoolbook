package com.marco.marplex.schoolbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.marco.marplex.schoolbook.utilities.Subjects;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ChartActivity extends AppCompatActivity {

    @Bind(R.id.chart) ColumnChartView chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ButterKnife.bind(this);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setZoomType(ZoomType.HORIZONTAL);

        ArrayList<com.marco.marplex.schoolbook.models.Materia> list =
                Subjects.getSubjects(this, getIntent().getIntExtra("period", 1));

        int numColumns = list.size();

        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue((float)list.get(i).mediaMateria, ChartUtils.pickColor()));

            axisValues.add(new AxisValue(i).setLabel(list.get(i).testoMateria));

            Column column = new Column(values);
            column.setHasLabels(true);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);

        Axis axisX = new Axis(axisValues);
        Axis axisY = new Axis().setHasLines(true);
        axisY.setName("Media");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        chart.setColumnChartData(data);
        chart.setZoomLevel(0,0, 5f);
    }
}
