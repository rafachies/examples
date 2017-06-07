package org.chies.fuse;

public class ItemLockedException extends RuntimeException{

	private static final long serialVersionUID = 7750697762062873808L;

	public ItemLockedException(String message) {
		super(message);
	}
}
