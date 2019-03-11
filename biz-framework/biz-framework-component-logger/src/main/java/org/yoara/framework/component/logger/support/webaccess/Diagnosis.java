package org.yoara.framework.component.logger.support.webaccess;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class Diagnosis {

    private static final ThreadLocal<Entry> entryStack = new ThreadLocal<Entry>();

    public static void start() {
        start(null);
    }

    public static void start(Object message) {
        entryStack.set(new Entry(message, null, null));
    }

    public static void enter(Object message) {
        Entry currentEntry = getCurrentEntry();
        if (currentEntry != null) {
            currentEntry.enterSubEntry(message);
        }
    }

    public static long getDuration() {
        Entry entry = entryStack.get();
        if (entry != null) {
            return entry.getDuration();
        } else {
            return -1;
        }
    }

    public static void release() {
        Entry currentEntry = getCurrentEntry();
        if (currentEntry != null) {
            currentEntry.release();
        }
    }

    public static void clear() {
        entryStack.set(null);
    }

    public static Entry getEntry() {
        return entryStack.get();
    }

    public static Entry getCurrentEntry() {
        Entry entry = entryStack.get();
        if (entry == null) {
            return null;
        }
        Entry subEntry = null;
        do {
            subEntry = entry.getUnreleasedEntry();
            if (subEntry != null) {
                entry = subEntry;
            }
        } while (subEntry != null);
        return entry;
    }

    public static String dump() {
        return dump("", "");
    }

    public static String dump(String prefix1) {
        return dump(prefix1, prefix1);
    }

    public static String dump(String prefix1, String prefix2) {
        Entry entry = (Entry) entryStack.get();
        if (entry != null) {
            return entry.toString(prefix1, prefix2);
        } else {
            return StringUtils.EMPTY;
        }
    }

    public static final class Entry {
        private List<Entry> subEntryList = new ArrayList<Entry>();
        private Object message;
        private Entry parentEntry;
        private Entry firstEntry;
        private long startTime;
        private long endTime;
        private long baseTime;

        private Entry(Object message, Entry parentEntry, Entry firstEntry) {
            super();
            this.message = message;
            this.parentEntry = parentEntry;
            this.firstEntry = (Entry) ObjectUtils.defaultIfNull(firstEntry, this);
            this.startTime = System.currentTimeMillis();
            this.baseTime = firstEntry == null ? 0 : firstEntry.startTime;
        }

        public String getMessage() {
            return message.toString();
        }

        public long getStartTime() {
            return this.baseTime > 0 ? this.startTime - this.baseTime : 0;
        }

        public long getEndTime() {
            if (this.endTime < this.baseTime) {
                return -1;
            } else {
                return this.endTime - this.baseTime;
            }
        }

        public long getDuration() {
            if (this.endTime < this.startTime) {
                return -1;
            } else {
                return this.endTime - this.startTime;
            }
        }

        public long getDurationOfSelf() {
            long duration = this.getDuration();
            for (Entry subEntry : subEntryList) {
                duration -= subEntry.getDuration();
            }
            return duration < 0 ? -1 : duration;
        }

        public double getPercentage() {
            double parentDuration = 0;
            double duration = this.getDuration();
            if (this.parentEntry != null) {
                parentDuration = this.parentEntry.getDuration();
            }
            if (duration > 0 && parentDuration > 0) {
                return duration / parentDuration;
            } else {
                return 0;
            }
        }

        public double getPercentageOfAll() {
            double firstDuration = 0;
            double duration = this.getDuration();
            if (this.firstEntry != null) {
                firstDuration = this.firstEntry.getDuration();
            }
            if (duration > 0 && firstDuration > 0) {
                return duration / firstDuration;
            } else {
                return 0;
            }
        }

        private boolean isReleased() {
            return this.endTime > 0;
        }

        private void release() {
            this.endTime = System.currentTimeMillis();
        }

        private void enterSubEntry(Object message) {
            Entry subEntry = new Entry(message, this, this.firstEntry);
            this.subEntryList.add(subEntry);
        }

        private Entry getUnreleasedEntry() {
            if (CollectionUtils.isEmpty(subEntryList)) {
                return null;
            }
            Entry subEntry = subEntryList.get(subEntryList.size() - 1);
            if (subEntry.isReleased()) {
                return null;
            } else {
                return subEntry;
            }
        }

        public String toString() {
            return toString("", "");
        }

        private String toString(String prefix1, String prefix2) {
            StringBuilder strBuilder = new StringBuilder();
            toString(strBuilder, prefix1, prefix2);
            return strBuilder.toString();
        }

        private void toString(StringBuilder strBuilder, String prefix1, String prefix2) {
            strBuilder.append(prefix1);
            String message = getMessage();
            long startTime = getStartTime();
            long duration = getDuration();
            long durationOfSelf = getDurationOfSelf();
            double percent = getPercentage();
            double percentOfAll = getPercentageOfAll();
            Object[] params = new Object[] { message, // {0} - entry信息
                                            new Long(startTime), // {1} - 起始时间
                                            new Long(duration), // {2} - 持续总时间
                                            new Long(durationOfSelf), // {3} - 自身消耗的时间
                                            new Double(percent), // {4} - 在父entry中所占的时间比例
                                            new Double(percentOfAll) // {5} - 在总时间中所旧的时间比例
            };

            StringBuffer pattern = new StringBuffer("{1,number} ");
            if (isReleased()) {
                pattern.append("[{2,number}ms");
                if ((durationOfSelf > 0) && (durationOfSelf != duration)) {
                    pattern.append(" ({3,number}ms)");
                }
                if (percent > 0) {
                    pattern.append(", {4,number,##%}");
                }
                if (percentOfAll > 0) {
                    pattern.append(", {5,number,##%}");
                }
                pattern.append("]");
            } else {
                pattern.append("[UNRELEASED]");
            }
            if (message != null) {
                pattern.append(" - {0}");
            }
            strBuilder.append(MessageFormat.format(pattern.toString(), params));
            for (int i = 0; i < subEntryList.size(); i++) {
                Entry subEntry = (Entry) subEntryList.get(i);
                strBuilder.append('\n');
                if (i == (subEntryList.size() - 1)) {
                    subEntry.toString(strBuilder, prefix2 + "`---", prefix2 + "    "); // 最后一项
                } else if (i == 0) {
                    subEntry.toString(strBuilder, prefix2 + "+---", prefix2 + "|   "); // 第一项
                } else {
                    subEntry.toString(strBuilder, prefix2 + "+---", prefix2 + "|   "); // 中间项
                }
            }
        }
    }
}