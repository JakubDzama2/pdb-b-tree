package sk.upjs.gursky.pdb;

import sk.upjs.gursky.bplustree.BPTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UnclusteredBPTree extends BPTree<PersonStringKey, SurnameAndOffsetEntry> {

    public static final int PAGE_SIZE = 4096;
    private File personsFile;

    private UnclusteredBPTree(File indexFile, File personsFile) {
        super(SurnameAndOffsetEntry.class, indexFile);
        this.personsFile = personsFile;
    }

    public static UnclusteredBPTree newTreeBulkLoading(File personsFile, File indexFile) throws IOException {
        UnclusteredBPTree tree = new UnclusteredBPTree(indexFile, personsFile);
        tree.setNodeSize(PAGE_SIZE);
        RandomAccessFile raf = new RandomAccessFile(personsFile, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(PAGE_SIZE);

        long fileSize = personsFile.length();
        long pageCount = fileSize / PAGE_SIZE;

        List<SurnameAndOffsetEntry> pairs = new ArrayList<SurnameAndOffsetEntry>();
        for (int offset = 0; offset < fileSize; offset += PAGE_SIZE) {
//            System.out.println("Citam " + (offset / PAGE_SIZE) + ". stranku");
            buffer.clear();
            channel.read(buffer, offset);
            buffer.rewind();
            int personsCount = buffer.getInt();
            for (int i = 0; i < personsCount; i++) {
                PersonEntry personEntry = new PersonEntry();
                personEntry.load(buffer);
                pairs.add(new SurnameAndOffsetEntry(personEntry.surname, offset + 4 + i * personEntry.getSize()));
            }
        }
        channel.close();
        raf.close();

        Collections.sort(pairs);

        tree.openAndBatchUpdate(pairs.iterator(), pairs.size());
        return tree;
    }

    public List<PersonEntry> intervalQueryEntries(PersonStringKey low, PersonStringKey high) throws IOException {
        List<SurnameAndOffsetEntry> pairs = super.intervalQuery(low, high);
        RandomAccessFile raf = new RandomAccessFile(personsFile, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(PAGE_SIZE);

        LinkedList<PersonEntry> entries = new LinkedList<>();
        for (SurnameAndOffsetEntry pair: pairs) {
            buffer.clear();
            long pageOffset = (pair.getOffset() / PAGE_SIZE) * PAGE_SIZE;
            int bufferOffset = (int) (pair.getOffset() - pageOffset);
            channel.read(buffer, pageOffset);
            buffer.position(bufferOffset);
            PersonEntry personEntry = new PersonEntry();
            personEntry.load(buffer);
            entries.add(personEntry);
        }
        channel.close();
        raf.close();
        return entries;
    }
}
