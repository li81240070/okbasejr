package com.hx.jrperson.bean.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * 订单时间。
 * Created by HX-SU CONG on 5/18/16.
 */
@SuppressWarnings("unused")
public class OrderDate {
    /**
     * 在WheelView中预留的天数是7天。
     */
    private static final int ALL_DAYS_COUNT = 7;

    /**
     * 在WheelView中使用的小时常量。
     */
    private static final int[] HOUR = {
            0, 1, 2, 3, 4, 5,
            6, 7, 8, 9, 10, 11,
            12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23};

    /**
     * 在WheelView中使用的分钟常量。
     */
    private static final int[] MIN = {
            0, 10, 20, 30, 40, 50
    };

    // 装载年月日的数组。
    private final int[] YMD_ARR = new int[3];

    /**
     * 小时的显示单位。
     */
    public static final String UNIT_HOUR = "点";

    /**
     * 分钟的显示单位。
     */
    public static final String UNIT_MIN = "分";

    /**
     * 日的显示单位。
     */
    private static final String UNIT_DAY = "号";

    /**
     * 当前系统时间。
     * <strong>一旦创建，就不可以改变了，以这个时间作为参照标准。</strong>
     */
    private final Date CUR_SYS_DATE;

    /**
     * 时间过滤时的格式化器。
     */
    public static final String DAY_FILTER_PATTERN = "yyyy_MM_dd_HH_mm_ss";

    /**
     * 时间分隔符。
     */
    public static final CharSequence SPLITTER = "_";

    /**
     * 显示几个天数。提供set方法。
     */
    private int showDaysCount = ALL_DAYS_COUNT;

    /**
     * 盛装标准时间的列表。
     */
    private List<Date> standardDates;

    // 显示天的列表。
    private List<String> daysShowInWheel;
    private List<String> curHoursList;
    private List<String> curMinList;

    // 用户选择的索引。
    private int userSelectIndex;

    // 初始化选中的小时。
    private int selectedHour;

    // 利用这个字符串来记录用户的选项，同时
    private UserRecorder userRecorder;

    /**
     * 构造器1：不提供对外时间方法，标准时间由OrderDate来决定。
     */
    public OrderDate() {
        CUR_SYS_DATE = new Date(System.currentTimeMillis());

        /* 将时间Date分离到数组中。 */
        String curSysDateToArr = getYearMonthDay(CUR_SYS_DATE, DAY_FILTER_PATTERN, SPLITTER.toString());
        for (int i = 0; i < curSysDateToArr.split(SPLITTER.toString()).length; i++) {
            YMD_ARR[i] = Integer.valueOf(curSysDateToArr.split(SPLITTER.toString())[i]);
        }

        // 初始化列表。
        standardDates = grtStandardDates(showDaysCount);
        daysShowInWheel = new ArrayList<>(showDaysCount);

        // 添加Date到列表中。
        addDateToList(showDaysCount);

        // 将在Wheel中展示的天 过滤出来。
        daysShowInWheel = filterDays(standardDates);

        // 创建选择器
        userRecorder = new UserRecorder();
    }

    /**
     * 构造器2：提供设置当前系统时间的参数。
     *
     * @param curSysDate 当前系统时间
     */
    public OrderDate(Date curSysDate) {
        CUR_SYS_DATE = curSysDate;
    }

    /**
     * 设置显示几个天数。默认是7天。
     *
     * @param daysCount 显示几个天数。
     */
    public void setShowDaysCount(int daysCount) {
        this.showDaysCount = daysCount;
    }

    /**
     * 生成一个标准Date的，指定长度的时间列表。
     *
     * @param daysCount 显示几个天数
     * @return 一个标准时间的列表
     */
    private List<Date> grtStandardDates(int daysCount) {
        if (daysCount < 0)
            throw new IllegalArgumentException("DaysCount can not be smaller than 0");

        return new ArrayList<>(daysCount);
    }

    /**
     * 日历的实例。
     */
    private static Calendar calendar;


    /**
     * 将标准日期添加到列表中。
     *
     * @param daysCount 显示几个天数
     */
    private void addDateToList(int daysCount) {
        // 设置Calendar的时间。
        calendar.setTime(CUR_SYS_DATE);

        // 将当天日期填入到列表中。
        standardDates.add(0, CUR_SYS_DATE);

        // daysCount - 1的原因，在上一行中将当天日期填入到列表中，然后列表中只剩余6个位置。
        for (int i = 1; i < daysCount; i++) {
            // params中的1 是向后的一天，也就是第二天。
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            // 添加到列表中。
            standardDates.add(i, calendar.getTime());

            // 将calendar设置到第二天。以便遍历循环。
            calendar.setTime(calendar.getTime());
        }
    }

    /**
     * 将标准的日期时间中的天这个字段过滤出来装到列表中，提供给WheelView来使用。
     *
     * @param standardDates 标准的日期时间。
     * @return 装着标准日期的“天”的列表。
     */
    private List<String> filterDays(List<Date> standardDates) {
        if (standardDates.isEmpty()) return Collections.emptyList();

        for (Date date : standardDates) {
            SimpleDateFormat sdf = new SimpleDateFormat(DAY_FILTER_PATTERN, Locale.getDefault());
            String dateString = sdf.format(date);

            // 将分离出来的时间放入集合
            final List<String> RESULT = Arrays.asList(dateString.split(SPLITTER.toString()));

            // 年的数据位置：0 月的数据位置：1 日的数据位置：2
            daysShowInWheel.add(RESULT.get(2));
        }

        // 修饰内容。
        return decorateFilteredDays(daysShowInWheel);
    }

    /**
     * 修饰天这列字符串，“今天”是第一个，不用加上“号”，之后的字符要加上“号”。
     *
     * @param filteredDays 过滤的天的字符列表。
     */
    private List<String> decorateFilteredDays(List<String> filteredDays) {
        /* 获取当前日期中的日期。 */
        SimpleDateFormat sdf = new SimpleDateFormat(DAY_FILTER_PATTERN, Locale.getDefault());
        String dayResult = sdf.format(CUR_SYS_DATE).split(SPLITTER.toString())[2];

        boolean isSameDay = false;

        /* 如果标准日期列表中的第一个天的数据 和 当前日期的天 相同的话，就列表中的第一个数据改为“今天”。 */
        LinkedList<String> asTempList = new LinkedList<>(filteredDays);
        if (dayResult.equals(asTempList.get(0))) {
            asTempList.removeFirst();
            asTempList.addFirst("今天");
            isSameDay = true;

            filteredDays = asTempList;
        }

        int starter = isSameDay ? 1 : 0;

        for (int i = starter; i < filteredDays.size(); i++) {
            filteredDays.set(i, filteredDays.get(i).concat(UNIT_DAY));
        }

        return filteredDays;
    }

    /**
     * WheelView中的第一列中的数据列表。
     *
     * @return 天的数据列表
     */
    public List<String> getDaysShowInWheel() {
        return daysShowInWheel;
    }

    /**
     * WheelView中的第二列中的数据列表。
     *
     * @return 小时的数据列表。
     */
    public List<String> getHoursShowInWheel() {
        String[] hoursArray = new String[HOUR.length];
        for (int i = 0; i < HOUR.length; i++) {
            hoursArray[i] = String.valueOf(HOUR[i]).concat(UNIT_HOUR);
        }

        return Arrays.asList(hoursArray);
    }

    /**
     * 判断用户选择的日期是不是今天。
     *
     * @param selectedDate 用户选择的日期
     * @return true：是今天 false：不是同一天。
     */
    public boolean isToday(Date selectedDate) {
        return getYearMonthDay(CUR_SYS_DATE, DAY_FILTER_PATTERN, SPLITTER.toString())
                .equals(getYearMonthDay(selectedDate, DAY_FILTER_PATTERN, SPLITTER.toString()));
    }

    /**
     * 判断当前日期的时间是否在当前的小时内。这影响了剩余分钟的数据列表。
     * 如果不是同一天，那肯定不是同一个小时内。
     *
     * @param isToady      是否是同一天
     * @return 是否是同一个小时内
     */
    public boolean isSameHour(boolean isToady) {
        if (!isToady) return false;
        int curHour = getHourInDate(CUR_SYS_DATE, DAY_FILTER_PATTERN, SPLITTER.toString());
        return selectedHour == curHour;
    }

    /**
     * 获得剩余小时的列表。
     *
     * @param isToday 是否当天
     * @return 剩余小时的列表
     */
    public List<String> remainsHours(boolean isToday) {
        if (!isToday) {
            // 如果默认不是当天，那么小时从0点开始，所以selectedHour的值就是0点这个小时。
            selectedHour = 0;
            return getHoursShowInWheel();
        } else {
            // 得到当前时间。
            int curHour = getHourInDate(CUR_SYS_DATE, DAY_FILTER_PATTERN, SPLITTER.toString());
            int curMin = getMinutesInDate(CUR_SYS_DATE, DAY_FILTER_PATTERN, SPLITTER.toString());

            /* 如果当前时间等于 23 点，那么当天就没有剩余的小时了。否则，就有剩余。 */
            if (curHour == HOUR[HOUR.length - 1]) {
                List<String> asTmpList = new ArrayList<>(1);
                if (curMin >= MIN[MIN.length - 1]) {
                    asTmpList.add("");
                } else {
                    asTmpList.add(String.valueOf(HOUR[HOUR.length - 1]).concat(UNIT_HOUR));
                }

                // 如果当前小时等于23点，那么默认当前选中的小时就是23点。
                selectedHour = HOUR[HOUR.length - 1];

                return asTmpList;
            } else {
                // curHour 算当前这个小时所以；
                final int[] leftHours;

                if (curMin >= MIN[MIN.length - 1]) {
                    // 如果每个小时都是50分钟之后的话，小时分钟都是满的。
                    leftHours = Arrays.copyOfRange(HOUR, curHour + 1, HOUR.length);
                } else {
                    leftHours = Arrays.copyOfRange(HOUR, curHour, HOUR.length);
                }

                // 默认列表中的第一个时间就是默认选中的时间。
                selectedHour = leftHours[0];

                String[] leftHoursArray = new String[leftHours.length];
                for (int i = 0; i < leftHoursArray.length; i++) {
                    leftHoursArray[i] = String.valueOf(leftHours[i]).concat(UNIT_HOUR);
                }

                return Arrays.asList(leftHoursArray);
            }
        }
    }

    /**
     * 获得剩余分钟的列表。
     *
     * @param isHour 是否为当前小时
     * @return 剩余分钟的列表
     */
    public List<String> remainsMin(boolean isHour) {
        if (!isHour) {
            return getMinShowInWheel();
        } else {
            int curMinutes = getMinutesInDate(CUR_SYS_DATE, DAY_FILTER_PATTERN, SPLITTER.toString());

            // 如果当前时间 >= 50分钟时候，这个小时内就没有分钟了。
            if (curMinutes >= MIN[MIN.length - 1]) {
                List<String> singleList = new ArrayList<>(1);
                singleList.add("");
                return singleList;
            } else {
                // rangePos说明了分钟的十位上的数在 MIN 中的位置。
                int rangePos = curMinutes / 10;
                final int[] leftMin = Arrays.copyOfRange(MIN, rangePos + 1, MIN.length);

                String[] leftMinArray = new String[leftMin.length];
                for (int i = 0; i < leftMinArray.length; i++) {
                    leftMinArray[i] = String.valueOf(leftMin[i]).concat(UNIT_MIN);
                }

                return Arrays.asList(leftMinArray);
            }
        }
    }

    /**
     * 初始化直接使用。
     *
     * @return 分钟列表。
     */
    public List<String> remainsMin() {
        int curMinutes = getMinutesInDate(CUR_SYS_DATE, DAY_FILTER_PATTERN, SPLITTER.toString());

        // 如果当前时间 >= 50分钟时候，这个小时内就没有分钟了。
        if (curMinutes >= MIN[MIN.length - 1]) {
            List<String> asTempList = new ArrayList<>(1);
            asTempList.add("");
            return asTempList;
        } else {
            // rangePos说明了分钟的十位上的数在 MIN 中的位置。
            int rangePos = curMinutes / 10;
            final int[] leftMin = Arrays.copyOfRange(MIN, rangePos + 1, MIN.length);

            String[] leftMinArray = new String[leftMin.length];
            for (int i = 0; i < leftMinArray.length; i++) {
                leftMinArray[i] = String.valueOf(leftMin[i]).concat(UNIT_MIN);
            }

            return Arrays.asList(leftMinArray);
        }
    }

    /**
     * 将标准时间的列表暴露在外部。
     *
     * @return 标准时间列表。
     */
    public List<Date> getStandardDates() {
        return standardDates;
    }

    /**
     * WheelView中的第二列中的数据列表。
     *
     * @return 小时的数据列表。
     */
    public List<String> getMinShowInWheel() {
        String[] minArray = new String[MIN.length];
        for (int i = 0; i < MIN.length; i++) {
            minArray[i] = String.valueOf(MIN[i]).concat(UNIT_MIN);
        }

        return Arrays.asList(minArray);
    }

    public int getUserSelectIndex() {
        return userSelectIndex;
    }

    public void setUserSelectIndex(int userSelectIndex) {
        this.userSelectIndex = userSelectIndex;
    }

    public UserRecorder getUserRecorder() {
        return userRecorder;
    }

    static {
        calendar = Calendar.getInstance(Locale.getDefault());
    }

    @Override
    public String toString() {
        return "OrderDate{" +
                "standardDates=" + standardDates +
                ", showDaysCount=" + showDaysCount +
                ", CUR_SYS_DATE=" + CUR_SYS_DATE +
                '}';
    }

    /**
     * 获取日期中的年月日。
     *
     * @param date     日期
     * @param pattern  日期格式符
     * @param splitter 分隔符
     * @return 年月日
     */
    public static String getYearMonthDay(Date date, String pattern, String splitter) {
        if (date == null) return "";

        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        String dateString = sdf.format(date);

        String[] ymd = dateString.split(splitter);

        return ymd[0] + "_" + ymd[1] + "_" + ymd[2];
    }

    /**
     * 获取日期中的小时。
     *
     * @param date     日期
     * @param pattern  日期格式符
     * @param splitter 分隔符
     * @return 小时
     */
    public static int getHourInDate(Date date, String pattern, String splitter) {
        if (date == null) return -1;

        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        String dateString = sdf.format(date);

        return Integer.valueOf(dateString.split(splitter)[3]);
    }

    /**
     * 获取日期中的分钟。
     *
     * @param date     日期
     * @param pattern  日期格式符
     * @param splitter 分隔符
     * @return 分钟
     */
    public static int getMinutesInDate(Date date, String pattern, String splitter) {
        if (date == null) return -1;

        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        String dateString = sdf.format(date);

        return Integer.valueOf(dateString.split(splitter)[4]);
    }

    public List<String> getCurHoursList() {
        return curHoursList;
    }

    public void setCurHoursList(List<String> curHoursList) {
        this.curHoursList = curHoursList;
    }

    public List<String> getCurMinList() {
        return curMinList;
    }

    public void setCurMinList(List<String> curMinList) {
        this.curMinList = curMinList;
    }

    public class UserRecorder {
        private String dateRecorder;
        private Date userDate;
        private int userSelection;
        private String userSelectedHour;
        private String userSelectedMin;
        private int hourSelection;
        private int minSelection;

        public UserRecorder() {
        }

        public String getDateRecorder() {
            return dateRecorder;
        }

        public void setDateRecorder(String dateRecorder) {
            this.dateRecorder = dateRecorder;
        }

        public Date getUserDate() {
            return userDate;
        }

        public void setUserDate(Date userDate) {
            this.userDate = userDate;
        }

        public int getUserSelection() {
            return userSelection;
        }

        public void setUserSelection(int userSelection) {
            this.userSelection = userSelection;
        }

        public boolean isSelectedToday(int userSelection) {
            return userSelection == 0;
        }

        public void setUserSelectedHour(String userSelectedHour) {
            this.userSelectedHour = userSelectedHour;
        }

        public void setUserSelectedMin(String userSelectedMin) {
            this.userSelectedMin = userSelectedMin;
        }

        public int getHourSelection() {
            return hourSelection;
        }

        public void setHourSelection(int hourSelection) {
            this.hourSelection = hourSelection;
        }

        public int getMinSelection() {
            return minSelection;
        }

        public void setMinSelection(int minSelection) {
            this.minSelection = minSelection;
        }

        @Override
        public String toString() {
            return "UserRecorder{" +
                    "minSelection=" + minSelection +
                    ", hourSelection=" + hourSelection +
                    ", userSelectedMin='" + userSelectedMin + '\'' +
                    ", userSelectedHour='" + userSelectedHour + '\'' +
                    ", userSelection=" + userSelection +
                    ", userDate=" + userDate +
                    ", dateRecorder='" + dateRecorder + '\'' +
                    '}';
        }
    }
}