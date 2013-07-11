package edu.umw.cpsc.magpie.core;

public enum GradingGroup {
	SCORE,
	TIMER,
    ALL;

	public String toDbString() {
		switch(this) {
		case SCORE:
			return "score";
		case TIMER:
			return "timer";
		case ALL:
			return "all";
		default:
			return "error";
		}
	}
}
