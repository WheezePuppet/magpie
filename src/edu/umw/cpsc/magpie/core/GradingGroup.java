package edu.umw.cpsc.magpie.core;

public enum GradingGroup {
	SCORE,
	TIMER,
    MULT_CHOICE,
    ALL;

	public String toDbString() {
		switch(this) {
		case SCORE:
			return "score";
		case TIMER:
			return "timer";
		case MULT_CHOICE:
			return "multi";
		case ALL:
			return "all";
		default:
			return "error";
		}
	}
}
