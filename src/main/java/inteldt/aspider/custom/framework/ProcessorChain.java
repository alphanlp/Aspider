package inteldt.aspider.custom.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理器链
 * 
 * @author pei
 *
 */
public class ProcessorChain extends Processor{
	private static List<Processor> preProcessrChain = new ArrayList<Processor>();
	private static List<Processor> fetcherChain = new ArrayList<Processor>();
	private static List<Processor> extractorChain = new ArrayList<Processor>();
	private static List<Processor> writerChain = new ArrayList<Processor>();
	
	public void addPreProcessr(Processor processor){
		preProcessrChain.add(processor);
	}
	
	public void addFetcher(Processor processor){
		fetcherChain.add(processor);
	}
	
	public void addExtractor(Processor processor){
		extractorChain.add(processor);
	}
	
	public void addWriter(Processor processor){
		writerChain.add(processor);
	}

	@Override
	protected boolean accept(CrawlerTask task) {
		if(task != null ){ // 任务不为空的时候，启用链处理
			return true;
		}
		return false;
	}

	@Override
	protected void acceptProcess(CrawlerTask task) {
		for(Processor processor : preProcessrChain){
			processor.process(task);
		}
		
		for(Processor processor : fetcherChain){
			processor.process(task);
		}
		
		for(Processor processor : extractorChain){
			processor.process(task);
		}
		
		for(Processor processor : writerChain){
			processor.process(task);
		}
	}

	@Override
	protected void rejectProcess(CrawlerTask task) {//TODO 
//		do nothing
	}

	
}
