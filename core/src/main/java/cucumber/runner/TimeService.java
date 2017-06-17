package cucumber.runner;

public interface TimeService {
    long startTime();
    long currentTime();
    long finishTime();

    TimeService SYSTEM = new TimeService() {
        @Override
        public long startTime() {
            return currentTime();
        }

        @Override
        public long currentTime() {
            return System.nanoTime();
        }

        @Override
        public long finishTime() {
            return currentTime();
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
            return currentTime();
        }

        @Override
        public long currentTime() {
            long result = getCurrentTime();
            result = incrementTimeIfstartTimeWasLastCall(result);
            return result;
        }

        private long getCurrentTime() {
            Long result = currentTime.get();
            return result != null ? result : 0l;
        }

        private long incrementTimeIfstartTimeWasLastCall(long result) {
            if (startTimeWasLastCall) {
                result += duration;
                currentTime.set(result);
                startTimeWasLastCall = false;
            }
            return result;
        }
    }
}
