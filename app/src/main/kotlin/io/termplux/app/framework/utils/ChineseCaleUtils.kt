package io.termplux.app.framework.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 获取中国农历
 */
class ChineseCaleUtils {

    // 农历的年份
    private var year = 0
    private var month = 0
    private var day = 0

    private var leap = false

    private var leapMonth = 0 // 闰的是哪个月

    // ====== 传回农历 y年的总天数 1900--2100
    private fun yearDays(y: Int): Int {
        var i: Int
        var sum = 348
        i = 0x8000
        while (i > 0x8) {
            if (lunarInfo[y - 1900] and i.toLong() != 0L) sum += 1
            i = i shr 1
        }
        return sum + leapDays(y)
    }

    // ====== 传回农历 y年闰月的天数
    private fun leapDays(y: Int): Int {
        return if (leapMonth(y) != 0) {
            if (lunarInfo[y - 1899] and 0xfL != 0L) 30 else 29
        } else 0
    }

    // ====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
    private fun leapMonth(y: Int): Int {
        val `var` = lunarInfo[y - 1900] and 0xfL
        return (if (`var` == 0xfL) 0 else `var`).toInt()
    }

    // ====== 传回农历 y年m月的总天数
    private fun monthDays(y: Int, m: Int): Int {
        return if (lunarInfo[y - 1900] and (0x10000 shr m).toLong() == 0L) 29 else 30
    }

    // ====== 传回农历 y年的生肖
    private fun animalsYear(year: Int): String {
        val animals = arrayOf(
            "鼠", "牛", "虎", "兔", "龙", "蛇",
            "马", "羊", "猴", "鸡", "狗", "猪"
        )
        return animals[(year - 4) % 12] + "年"
    }

    // ====== 传入 offset 传回干支, 0=甲子
    private fun cyclical(year: Int, month: Int, day: Int): String {
        var num = year - 1900 + 36
        //立春日期
        val term2 = sTerm(year, 2)
        num = if (month > 2 || month == 2 && day >= term2) {
            num + 0
        } else {
            num - 1
        }
        return cyclical(num)
    }

    /*
     * 计算公历nY年nM月nD日和bY年bM月bD日渐相差多少天
     * */
    private fun getDaysOfTwoDate(
        nY: Int,
        nM: Int,
        nD: Int
    ): Int {
        var baseDate: Date? = null
        var nowadays: Date? = null
        try {
            baseDate =
                chineseDateFormat.parse(1900.toString() + "年" + 1 + "月" + 31 + "日")
            nowadays =
                chineseDateFormat.parse(nY.toString() + "年" + nM + "月" + nD + "日")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        // 求出相差的天数
        return ((nowadays!!.time - baseDate!!.time) / 86400000L).toInt()
    }

    /*农历lunYear年lunMonth月lunDay日
     * isLeap 当前年月是否是闰月
     * 从农历日期转换成公历日期
     * */
    private fun getSunDate(lunYear: Int, lunMonth: Int, lunDay: Int): Calendar {
        //公历1900年1月31日为1900年正月初一
        val years = lunYear - 1900
        var days = 0
        for (i in 0 until years) {
            days += yearDays(1900 + i) //农历某年总天数
        }
        for (i in 1 until lunMonth) {
            days += monthDays(lunYear, i)
        }
        if (leapMonth(lunYear) != 0 && lunMonth > leapMonth(lunYear)) {
            days += leapDays(lunYear) //lunYear年闰月天数
        }
        days += lunDay
        days -= 1
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = 1900
        cal[Calendar.MONTH] = 0
        cal[Calendar.DAY_OF_MONTH] = 31
        cal.add(Calendar.DATE, days)
        return cal
    }

    private fun getLunarString(year: Int, month: Int, day: Int): String {
        val `var` = getLunarDateINT(year, month, day)
        val lYear = `var` / 10000
        val lMonth = (`var` % 10000) / 100
        val lDay = `var` - lYear * 10000 - lMonth * 100
        cyclical(year, month, day) + "年"
        var lunM = ""
        val testMonth = getSunDate(lYear, lMonth, lDay)[Calendar.MONTH] + 1
        if (testMonth != month) {
            lunM = "闰"
        }
        lunM += chineseNumber[(lMonth - 1) % 12] + "月"
        val lunD = getChinaDayString(lDay)
        getFestival(year, month, day)
        return "$lunM $lunD "
    }

    /*
     * 将公历year年month月day日转换成农历
     * 返回格式为20140506（int）
     * */
    private fun getLunarDateINT(year: Int, month: Int, day: Int): Int {
        val lYear: Int
        val lMonth: Int
        val lDay: Int
        var daysOfYear = 0
        // 求出和1900年1月31日相差的天数
        //year =1908;
        //month = 3;
        //day =3;
        var offset = getDaysOfTwoDate(year, month, day)
        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        var iYear = 1900
        while (iYear < 2100 && offset > 0) {
            daysOfYear = yearDays(iYear)
            offset -= daysOfYear
            iYear++
        }
        if (offset < 0) {
            offset += daysOfYear
            iYear--
        }
        // 农历年份
        lYear = iYear
        leapMonth = leapMonth(iYear) // 闰哪个月,1-12
        leap = false

        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        var daysOfMonth = 0
        var iMonth = 1
        while (iMonth < 13 && offset > 0) {

            // 闰月
            if (leapMonth > 0 && iMonth == leapMonth + 1 && !leap) {
                --iMonth
                leap = true
                daysOfMonth = leapDays(iYear)
            } else {
                daysOfMonth = monthDays(iYear, iMonth)
            }
            // 解除闰月
            if (leap && iMonth == leapMonth + 1) leap = false
            offset -= daysOfMonth
            iMonth++
        }
        // offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false
            } else {
                leap = true
                --iMonth
            }
        }
        // offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth
            --iMonth
        }
        lMonth = iMonth
        lDay = offset + 1
        return lYear * 10000 + lMonth * 100 + lDay
    }

    private fun getFestival(year: Int, month: Int, day: Int): String { //获取公历对应的节假日或二十四节气

        //农历节假日
        val `var` = getLunarDateINT(year, month, day)
        val lunYear = `var` / 10000
        val lunMonth = (`var` % 10000) / 100
        val lunDay = (`var` - lunYear * 10000 - lunMonth * 100)
        for (i in lunarHoliday.indices) {
            // 返回农历节假日名称
            val ld = lunarHoliday[i].split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0] // 节假日的日期
            val ldv = lunarHoliday[i].split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1] // 节假日的名称
            var monthV = lunMonth.toString() + ""
            var dayV = lunDay.toString() + ""
            if (month < 10) {
                monthV = "0$lunMonth"
            }
            if (day < 10) {
                dayV = "0$lunDay"
            }
            val lmd: String = monthV + dayV
            if (ld.trim { it <= ' ' } == lmd.trim { it <= ' ' }) {
                return ldv
            }
        }

        //公历节假日
        for (i in solarHoliday.indices) {
            if ((i == solarHoliday.size && year < 1893 || i + 3 == solarHoliday.size && year < 1999 || i + 6 == solarHoliday.size && year < 1942 || i + 10 == solarHoliday.size && year < 1949 || i == 19) && year < 1921 || i == 20 && year < 1933 || i == 22 && year < 1976) {
                break
            }
            // 返回公历节假日名称
            val sd = solarHoliday[i].split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0] // 节假日的日期
            val sdv = solarHoliday[i].split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1] // 节假日的名称
            var monthV = month.toString() + ""
            var stayV = day.toString() + ""
            if (month < 10) {
                monthV = "0$month"
            }
            if (day < 10) {
                stayV = "0$day"
            }
            val smd: String = monthV + stayV
            if (sd.trim { it <= ' ' } == smd.trim { it <= ' ' }) {
                return sdv
            }
        }
        val b = getDateOfSolarTerms(year, month)
        if (day == b / 100) {
            return sTermInfo[(month - 1) * 2]
        } else if (day == b % 100) {
            return sTermInfo[(month - 1) * 2 + 1]
        }
        return ""
    }

    /**  */

    override fun toString(): String {
        return if (chineseNumber[(month - 1) % 12] === "正" && getChinaDayString(day) === "初一") "农历" + year + "年" else if (getChinaDayString(
                day
            ) === "初一"
        ) chineseNumber[(month - 1) % 12] + "月" else getChinaDayString(day)
    }

    private fun sTerm(y: Int, n: Int): Int {
        val sTermInfo = intArrayOf(
            0, 21208, 42467, 63836, 85337, 107014,
            128867, 150921, 173149, 195551, 218072, 240693,
            263343, 285989, 308563, 331033, 353350, 375494,
            397447, 419210, 440795, 462224, 483532, 504758
        )
        val cal = Calendar.getInstance()
        cal[1900, 0, 6, 2, 5] = 0
        val temp = cal.time.time
        cal.time =
            Date((31556925974.7 * (y - 1900) + sTermInfo[n] * 60000L + temp).toLong())
        return cal[Calendar.DAY_OF_MONTH]
    }

    private fun getDateOfSolarTerms(year: Int, month: Int): Int {
        val a = sTerm(year, (month - 1) * 2)
        val b = sTerm(year, (month - 1) * 2 + 1)
        return a * 100 + b
    }

    // ====== 传入 月日的offset 传回干支, 0=甲子
    private fun cyclical(num: Int): String {
        return Gan[num % 10] + Zhi[num % 12]
    }

    //将农历day日格式化成农历表示的字符串
    private fun getChinaDayString(day: Int): String {
        val chineseTen = arrayOf("初", "十", "廿", "卅")
        val chineseDay = arrayOf("一", "二", "三", "四", "五", "六", "七", "八", "九", "十")
        return if (day != 20 && day != 30) {
            chineseTen[((day - 1) / 10)] + chineseDay[((day - 1) % 10)]
        } else if (day != 20) {
            chineseTen[(day / 10)] + "十"
        } else {
            "二十"
        }
    }

    companion object {

        private val chineseNumber = arrayOf(
            "正", "二", "三", "四", "五", "六", "七",
            "八", "九", "十", "冬", "腊"
        )

        @SuppressLint("SimpleDateFormat")
        private var chineseDateFormat = SimpleDateFormat(
            "yyyy年MM月dd日"
        )
        private val lunarInfo = longArrayOf(
            0x4bd8, 0x4ae0, 0xa570, 0x54d5, 0xd260, 0xd950, 0x5554, 0x56af, 0x9ad0, 0x55d2,
            0x4ae0, 0xa5b6, 0xa4d0, 0xd250, 0xd255, 0xb54f, 0xd6a0, 0xada2, 0x95b0, 0x4977,
            0x497f, 0xa4b0, 0xb4b5, 0x6a50, 0x6d40, 0xab54, 0x2b6f, 0x9570, 0x52f2, 0x4970,
            0x6566, 0xd4a0, 0xea50, 0x6a95, 0x5adf, 0x2b60, 0x86e3, 0x92ef, 0xc8d7, 0xc95f,
            0xd4a0, 0xd8a6, 0xb55f, 0x56a0, 0xa5b4, 0x25df, 0x92d0, 0xd2b2, 0xa950, 0xb557,
            0x6ca0, 0xb550, 0x5355, 0x4daf, 0xa5b0, 0x4573, 0x52bf, 0xa9a8, 0xe950, 0x6aa0,
            0xaea6, 0xab50, 0x4b60, 0xaae4, 0xa570, 0x5260, 0xf263, 0xd950, 0x5b57, 0x56a0,
            0x96d0, 0x4dd5, 0x4ad0, 0xa4d0, 0xd4d4, 0xd250, 0xd558, 0xb540, 0xb6a0, 0x95a6,
            0x95bf, 0x49b0, 0xa974, 0xa4b0, 0xb27a, 0x6a50, 0x6d40, 0xaf46, 0xab60, 0x9570,
            0x4af5, 0x4970, 0x64b0, 0x74a3, 0xea50, 0x6b58, 0x5ac0, 0xab60, 0x96d5, 0x92e0,
            0xc960, 0xd954, 0xd4a0, 0xda50, 0x7552, 0x56a0, 0xabb7, 0x25d0, 0x92d0, 0xcab5,
            0xa950, 0xb4a0, 0xbaa4, 0xad50, 0x55d9, 0x4ba0, 0xa5b0, 0x5176, 0x52bf, 0xa930,
            0x7954, 0x6aa0, 0xad50, 0x5b52, 0x4b60, 0xa6e6, 0xa4e0, 0xd260, 0xea65, 0xd530,
            0x5aa0, 0x76a3, 0x96d0, 0x4afb, 0x4ad0, 0xa4d0, 0xd0b6, 0xd25f, 0xd520, 0xdd45,
            0xb5a0, 0x56d0, 0x55b2, 0x49b0, 0xa577, 0xa4b0, 0xaa50, 0xb255, 0x6d2f, 0xada0,
            0x4b63, 0x937f, 0x49f8, 0x4970, 0x64b0, 0x68a6, 0xea5f, 0x6b20, 0xa6c4, 0xaaef,
            0x92e0, 0xd2e3, 0xc960, 0xd557, 0xd4a0, 0xda50, 0x5d55, 0x56a0, 0xa6d0, 0x55d4,
            0x52d0, 0xa9b8, 0xa950, 0xb4a0, 0xb6a6, 0xad50, 0x55a0, 0xaba4, 0xa5b0, 0x52b0,
            0xb273, 0x6930, 0x7337, 0x6aa0, 0xad50, 0x4b55, 0x4b6f, 0xa570, 0x54e4, 0xd260,
            0xe968, 0xd520, 0xdaa0, 0x6aa6, 0x56df, 0x4ae0, 0xa9d4, 0xa4d0, 0xd150, 0xf252,
            0xd520
        )

        // 农历部分假日
        private val lunarHoliday = arrayOf(
            "0101 春节",
            "0115 元宵节",
            "0505 端午节",
            "0707 七夕情人节",
            "0715 中元节 孟兰节",
            "0730 地藏节",
            "0802 灶君诞",
            "0815 中秋节",
            "0827 先师诞",
            "0909 重阳节",
            "1208 腊八节  释迦如来成道日",
            "1223 小年",
            "0100 除夕"
        )

        // 公历部分节假日
        private val solarHoliday = arrayOf(
            "0101 元旦",
            "0110 中国110宣传日",
            "0214 情人",
            "0221 国际母语日",
            "0303 国际爱耳日",
            "0308 妇女节",
            "0312 植树节",
            "0315 消费者权益日",
            "0322 世界水日",
            "0323 世界气象日",
            "0401 愚人节",
            "0407 世界卫生日",
            "0501 劳动节",
            "0504 青年节",
            "0512 护士节",
            "0519 全国助残日",
            "0531 世界无烟日",
            "0601 儿童节",
            "0626 国际禁毒日",
            "0701 建党节",  //1921
            "0801 建军节",  //1933
            "0808 父亲节",// //
            "0909 毛泽东逝世纪念",  //1976
            "0910 教师节",
            "0917 国际和平日",
            "0927 世界旅游日",
            "0928 孔子诞辰",
            "1001 国庆节",
            "1006 老人节",
            "1007 国际住房日",
            "1014 世界标准日",
            "1024 联合国日",
            "1112 孙中山诞辰纪念",
            "1210 世界人权日",
            "1220 澳门回归纪念",
            "1224 平安夜",
            "1225 圣诞节",
            "1226 毛泽东诞辰纪念"
        )

        //24节气
        // 时节     气候
        private val sTermInfo = arrayOf(
            "小寒", "大寒",
            "立春", "雨水",
            "惊蛰", "春分",
            "清明", "谷雨",
            "立夏", "小满",
            "芒种", "夏至",
            "小暑", "大暑",
            "立秋", "处暑",
            "白露", "秋分",
            "寒露", "霜降",
            "立冬", "小雪",
            "大雪", "冬至"
        )

        private val Gan = arrayOf(
            "甲", "乙", "丙", "丁", "戊", "己", "庚",
            "辛", "壬", "癸"
        )

        private val Zhi = arrayOf(
            "子", "丑", "寅", "卯", "辰", "巳", "午",
            "未", "申", "酉", "戌", "亥"
        )

        // 入口函数，返回农历日期的字符串
        fun getChineseCale(): String {
            val calendar = Calendar.getInstance()

            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH] + 1
            val day = calendar[Calendar.DATE]

            val lunarGanZhi = ChineseCaleUtils().cyclical(year, month, day)
            val lunarAnimal = ChineseCaleUtils().animalsYear(year)
            val lunarString = ChineseCaleUtils().getLunarString(year, month, day)

            return "$lunarGanZhi\t$lunarAnimal\t$lunarString"
        }
    }
}