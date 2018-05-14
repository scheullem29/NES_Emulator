package SE.project.nes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

class DataHelper {
    StringProperty ctr = new SimpleStringProperty();
    StringProperty cycle = new SimpleStringProperty();
    StringProperty memory = new SimpleStringProperty();
    StringProperty accumulator = new SimpleStringProperty();
    StringProperty carry = new SimpleStringProperty();
    StringProperty zero = new SimpleStringProperty();
    StringProperty interrupt = new SimpleStringProperty();
    StringProperty decimal = new SimpleStringProperty();
    StringProperty brk = new SimpleStringProperty();
    StringProperty overflow = new SimpleStringProperty();
    StringProperty sign = new SimpleStringProperty();
    StringProperty regX = new SimpleStringProperty();
    StringProperty regY = new SimpleStringProperty();

    DataHelper(String ctr, String cycle, String memory, String carry, String zero, String interrupt, String decimal, String brk, String overflow, String sign, String accumulator, String regX, String regY) {
        this.ctr.setValue(ctr);
        this.cycle.setValue(cycle);
        this.memory.setValue(memory);
        this.accumulator.setValue(accumulator);
        this.carry.setValue(carry);
        this.zero.setValue(zero);
        this.interrupt.setValue(interrupt);
        this.decimal.setValue(decimal);
        this.brk.setValue(brk);
        this.overflow.setValue(overflow);
        this.sign.setValue(sign);
        this.regX.setValue(regX);
        this.regY.setValue(regY);
    }
    
    public String getCtr(){
        return ctr.get();
    }
    
    public StringProperty ctrProperty(){
        return ctr;
    }
    
    public void setCtr(String ctr){
        this.ctr.set(ctr);
    }
    
    public String getcycle(){
        return cycle.get();
    }
    
    public StringProperty cycleProperty(){
        return cycle;
    }
    
    public void setCycle(String cycle){
        this.cycle.set(cycle);
    }
    
    public String getmemory(){
        return memory.get();
    }
    
    public StringProperty memoryProperty(){
        return memory;
    }
    
    public void setmemory(String memory){
        this.memory.set(memory);
    }
    
    public String getaccumulator(){
        return accumulator.get();
    }
    
    public StringProperty accumulatorProperty(){
        return accumulator;
    }
    
    public void setaccumulator(String accumulator){
        this.accumulator.set(accumulator);
    }
    
    public String getregX(){
        return regX.get();
    }
    
    public StringProperty regXProperty(){
        return regX;
    }
    
    public void setregX(String regX){
        this.regX.set(regX);
    }
    
    public String getregY(){
        return regY.get();
    }
    
    public StringProperty regYProperty(){
        return regY;
    }
    
    public void setregY(String regY){
        this.regY.set(regY);
    }
    
    public String getcarry(){
        return carry.get();
    }
    
    public StringProperty carryProperty(){
        return carry;
    }
    
    public void setcarry(String carry){
        this.carry.set(carry);
    }
    
    public String getzero(){
        return zero.get();
    }
    
    public StringProperty zeroProperty(){
        return zero;
    }
    
    public void setzero(String zero){
        this.zero.set(zero);
    }
    
    public String getinterrupt(){
        return interrupt.get();
    }
    
    public StringProperty interruptProperty(){
        return interrupt;
    }
    
    public void setinterrupt(String interrupt){
        this.interrupt.set(interrupt);
    }
    
    public String getdecimal(){
        return decimal.get();
    }
    
    public StringProperty decimalProperty(){
        return decimal;
    }
    
    public void setdecimal(String decimal){
        this.decimal.set(decimal);
    }
    
    public String getbrk(){
        return brk.get();
    }
    
    public StringProperty brkProperty(){
        return brk;
    }
    
    public void setbrk(String brk){
        this.brk.set(brk);
    }
    
    public String getoverflow(){
        return overflow.get();
    }
    
    public StringProperty overflowProperty(){
        return overflow;
    }
    
    public void setoverflow(String overflow){
        this.overflow.set(overflow);
    }
    
    public String getsign(){
        return sign.get();
    }
    
    public StringProperty signProperty(){
        return sign;
    }
    
    public void setsign(String sign){
        this.sign.set(sign);
    }
}
