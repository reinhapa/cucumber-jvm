package cucumber.runner;

public interface TimeService {
    long startTime();
    long currentTime();
    long finishTime();

    TimeService SYSTEM = new TimeService() {
        @Override
        public long startTime() {
            return System.nanoTime();
        }

        @Override
        public long currentTime() {
            return System.nanoTime();
        }

        @Override
        public long finishTime() {
            return System.nanoTime();
        }
    };

    class Stub implements TimeService {
        private final long duration;
        private final ThreadLocal<Long> currentTime = new ThreadLocal<Long>();
        private boolean startTimeWasLastCall = false;

        public Stub(long duration) {
            this.duration = duration;
        }

        @Override
        public long startTime() {
            startTimeWasLastCall = true;
            return getCurrentTime();
        }

        @Override
        public long finishTime() {
            long result = getCurrentTime();
            if (startTimeWasLastCall) {
                result += duration;
                currentTime.set(result);
            }
            startTimeWasLastCall = false;
            return result;
        }

        @Override
        public long currentTime() {
            return getCurrentTime();
        }

        private long getCurrentTime() {
            Long result = currentTime.get();
            return result != null ? result : 0l;
        }
    }
}
