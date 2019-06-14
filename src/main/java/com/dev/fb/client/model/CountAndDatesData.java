package com.dev.fb.client.model;

import java.util.List;

/**
 * Created by Debojit Deb on 11 Feb, 2018.
 */
public class CountAndDatesData implements Comparable<CountAndDatesData> {

    private Integer count;
    private List<String> dates;

    public CountAndDatesData(Integer count, List<String> dates) {

        this.count = count;
        this.dates = dates;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CountAndDatesData that = (CountAndDatesData) o;

        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        return !(dates != null ? !dates.equals(that.dates) : that.dates != null);
    }

    @Override
    public int hashCode() {

        int result = count != null ? count.hashCode() : 0;
        result = 31 * result + (dates != null ? dates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CountAndDatesData{" +
                "count=" + count +
                ", dates=" + dates +
                '}';
    }

    //@Override
    public int compareTo(CountAndDatesData o) {

		return o.count.compareTo(this.count);
	}

}