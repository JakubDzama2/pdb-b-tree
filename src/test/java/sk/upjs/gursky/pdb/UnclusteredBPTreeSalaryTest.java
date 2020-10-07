package sk.upjs.gursky.pdb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class UnclusteredBPTreeSalaryTest {

	private static final File INDEX_FILE = new File("person.unkl");
	private UnclusteredBPTreeSalary bptree;
	
	@Before
	public void setUp() throws Exception {
		bptree = UnclusteredBPTreeSalary.newTreeBulkLoading(Generator.GENERATED_FILE, INDEX_FILE);
	}

	@After
	public void tearDown() throws Exception {
		bptree.close();
      	INDEX_FILE.delete();
	}

	@Test
	public void test() throws Exception {	
		long time = System.currentTimeMillis();
		List<PersonEntry> result = bptree.intervalQueryEntries(new SalaryKey(1000), new SalaryKey(1200));
		time = System.currentTimeMillis() - time;
		
		System.out.println(time);
		for (int i = 0; i < 10; i++) {
			System.out.println(result.get(i));
		}

		assertTrue(result.size() > 0);
	}
}
