package cbonoan.a9;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.text.NumberFormat;
import java.util.Locale;

public class Coin  extends BaseObservable {
    private String name;
    private double curValue;

    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public Coin(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getCurValue() {
        return numberFormat.format(curValue);
    }

    public void setCurValue(double curValue) {
        this.curValue = curValue;
        notifyPropertyChanged(BR.curValue);
    }
}
