package zcc.es.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class SnowIdUtils {
    private final long twepoch;
    private final long workerIdBits;
    private final long dataCenterIdBits;
    private final long maxWorkerId;
    private final long maxDataCenterId;
    private final long sequenceBits;
    private final long workerIdShift;
    private final long dataCenterIdShift;
    private final long timestampLeftShift;
    private final long sequenceMask;
    private long workerId;
    private long dataCenterId;
    private long sequence;
    private long lastTimestamp;

    public static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            int[] var3 = ints;
            int var4 = ints.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                int b = var3[var5];
                sums += b;
            }

            return (long)(sums % 32);
        } catch (UnknownHostException var7) {
            return RandomUtils.nextLong(0L, 31L);
        }
    }

    public static Long getDataCenterId() {
        try {
            String hostName = Inet4Address.getLocalHost().getCanonicalHostName();
            int[] ints = StringUtils.toCodePoints(hostName);
            int sums = 0;
            int[] var3 = ints;
            int var4 = ints.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                int i = var3[var5];
                sums += i;
            }

            return (long)(sums % 32);
        } catch (UnknownHostException var7) {
            return RandomUtils.nextLong(0L, 31L);
        }
    }

    private SnowIdUtils(long workerId, long dataCenterId) {
        this.twepoch = 1489111610226L;
        this.workerIdBits = 5L;
        this.dataCenterIdBits = 5L;
        this.maxWorkerId = 31L;
        this.maxDataCenterId = 31L;
        this.sequenceBits = 12L;
        this.workerIdShift = 12L;
        this.dataCenterIdShift = 17L;
        this.timestampLeftShift = 22L;
        this.sequenceMask = 4095L;
        this.sequence = 0L;
        this.lastTimestamp = -1L;
        if (workerId <= 31L && workerId >= 0L) {
            if (dataCenterId <= 31L && dataCenterId >= 0L) {
                this.workerId = workerId;
                this.dataCenterId = dataCenterId;
            } else {
                throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", 31L));
            }
        } else {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", 31L));
        }
    }

    private synchronized long nextId() {
        long timestamp = this.timeGen();
        if (timestamp < this.lastTimestamp) {
            long offset = this.lastTimestamp - timestamp;
            if (offset > 5L) {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", timestamp));
            }

            try {
                this.wait(offset << 1);
                timestamp = this.timeGen();
                if (timestamp < this.lastTimestamp) {
                    throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
                }
            } catch (InterruptedException var6) {
                throw new RuntimeException(var6);
            }
        }

        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1L & 4095L;
            if (this.sequence == 0L) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0L;
        }

        this.lastTimestamp = timestamp;
        return timestamp - 1489111610226L << 22 | this.dataCenterId << 17 | this.workerId << 12 | this.sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static synchronized Long generateId() {
        long id = SingletonEnum.INSTANCE.getInstance().nextId();
        return id;
    }

    static enum SingletonEnum {
        INSTANCE;

        private SnowIdUtils snowflakeId = new SnowIdUtils(SnowIdUtils.getWorkId(), SnowIdUtils.getDataCenterId());

        private SingletonEnum() {
        }

        public SnowIdUtils getInstance() {
            return this.snowflakeId;
        }
    }
}
