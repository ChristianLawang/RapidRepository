package util;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import controller.MenuController;

/**
 *
 * @author HP
 */
public class DtoBroadcaster {
    
    //DtoBroadcaster.broadcast(this.getClass(), "anu", 1L);
    
    public static<T> void broadcast(Object obj, String idDto, T handleParam)
    {
        if(obj!=null)
        {
            try{
                for (Method method : obj.getClass().getMethods())
                {
                    if (method.isAnnotationPresent((Class<? extends Annotation>) DtoListener.class))
                    {
                        DtoListener ta = method.getAnnotation(DtoListener.class);   //Mendapatkan parameter dari bus listener
                        //System.out.println("Method = "+obj.getClass().getName()+"::"+method.getName());
                        if(ta.idDtoListener().contains(idDto))
                        {
                            if(handleParam!=null)
                            {
                                Object iClass = obj;
                                Class params[] = new Class[1];
                                params[0] = handleParam.getClass();
                                Method m = obj.getClass().getDeclaredMethod(method.getName(), params);
                                m.invoke(iClass, handleParam);
                            }
                            else
                            {
                                Object iClass = obj;
                                Class params[] = {};
                                Object paramsObj[] = {};
                                Method m = obj.getClass().getDeclaredMethod(method.getName(), params);
                                m.invoke(iClass, paramsObj);
                            }
                        }
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        //System.out.println("DtoBroadcaster::broadcast kelas null, tidak bisa manggil method");
    }
}