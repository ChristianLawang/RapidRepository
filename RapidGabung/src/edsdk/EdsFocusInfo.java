package edsdk;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
/**
 * <i>native declaration : canonsdk-2.9\Windows\EDSDK\Header\EDSDKTypes.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class EdsFocusInfo extends Structure {
	/// C type : EdsRect
	public EdsRect imageRect;
	/// C type : EdsUInt32
	public NativeLong pointNumber;
	/// C type : EdsFocusPoint[128]
	public EdsFocusPoint[] focusPoint = new EdsFocusPoint[(128)];
	/// C type : EdsUInt32
	public NativeLong executeMode;
	public EdsFocusInfo() {
		super();
		initFieldOrder();
	}
	protected void initFieldOrder() {
		setFieldOrder(new java.lang.String[]{"imageRect", "pointNumber", "focusPoint", "executeMode"});
	}
	/**
	 * @param imageRect C type : EdsRect<br>
	 * @param pointNumber C type : EdsUInt32<br>
	 * @param focusPoint C type : EdsFocusPoint[128]<br>
	 * @param executeMode C type : EdsUInt32
	 */
	public EdsFocusInfo(EdsRect imageRect, NativeLong pointNumber, EdsFocusPoint focusPoint[], NativeLong executeMode) {
		super();
		this.imageRect = imageRect;
		this.pointNumber = pointNumber;
		if (focusPoint.length != this.focusPoint.length) 
			throw new IllegalArgumentException("Wrong array size !");
		this.focusPoint = focusPoint;
		this.executeMode = executeMode;
		initFieldOrder();
	}
	public static class ByReference extends EdsFocusInfo implements Structure.ByReference {
		
	};
	public static class ByValue extends EdsFocusInfo implements Structure.ByValue {
		
	};
}
