package sk.upjs.gursky.pdb;

import sk.upjs.gursky.bplustree.BPObject;

import java.nio.ByteBuffer;

public class SalaryAndOffsetEntry implements BPObject<SalaryKey, SalaryAndOffsetEntry> {

    private int salary; // 4B
    private long offset; // 8B

    public SalaryAndOffsetEntry() {
    }

    public SalaryAndOffsetEntry(int salary, long offset) {
        this.salary = salary;
        this.offset = offset;
    }

    public int getSalary() {
        return salary;
    }

    public long getOffset() {
        return offset;
    }

    @Override
    public void load(ByteBuffer bb) {
        salary = bb.getInt();
        offset = bb.getLong();
    }

    @Override
    public void save(ByteBuffer bb) {
        bb.putInt(salary);
        bb.putLong(offset);
    }

    @Override
    public int getSize() {
        return 12;
    }

    @Override
    public SalaryKey getKey() {
        return new SalaryKey(salary);
    }

    @Override
    public int compareTo(SalaryAndOffsetEntry o) {
        return this.getKey().compareTo(o.getKey());
    }

    @Override
    public String toString() {
        return "SalaryAndOffsetEntry{" +
                "salary=" + salary +
                ", offset=" + offset +
                '}';
    }
}
