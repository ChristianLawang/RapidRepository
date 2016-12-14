package utilfx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import util.Kodeable;

/**
 *
 * @author HP
 */
public class Comboboks<T extends Kodeable> extends ComboBox<T>{
    
    //Ini harus make T supaya semua kelas referensi(msl referensiTujuanTpb dll) bisa masuk ke fungsi setItemComboboks
    //Ditambahkan extend Kodeable supaya di fungsi setItemComboboks bisa make t.getKode dan t.getUraian
    
    public Comboboks()
    {
        super();
    }
    

	public void setItemComboboks(List<T> inputData)
    {
        setConverter(new StringConverter<T>() {
            //Ini fungsinya supaya di komboboks hasilnya kode. uraian msl 1. Gudang Berikat
            @Override
            public String toString(T t) {
                return String.format("%s - %s", t.getKode(), t.getUraian());
            }

            @Override
            public T fromString(String string) {
                return null;
            }
        });
        //Memasukkan semua data ke komboboks
        this.getItems().addAll(inputData);
    }
    
    public String getSelectedKode() {
        if (getSelectionModel() != null && getSelectionModel().getSelectedItem() != null) {
            Object o = getSelectionModel().getSelectedItem();
            T t = (T) o;
            return t.getKode();
        }
        return null;
    }
    
    public void selectKode(String kode) {
        ObservableList items = getItems();
        for (Iterator it = items.iterator(); it.hasNext();) {
            Object o = it.next();
            T t = (T) o;
            if (kode != null) {
                if (kode.equalsIgnoreCase(t.getKode())) {
                    setValue(t);
                    return;
                }
            }
        }
    }
    
    public void setEditableCombo(boolean b)
    {
        setDisable(!b);
        if(!b)
        {
            setStyle("-fx-opacity: 0.8;");
        }
    }
}
