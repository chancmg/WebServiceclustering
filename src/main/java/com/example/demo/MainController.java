package com.example.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.MakeDensityBasedClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVSaver;
import weka.core.converters.JSONSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;
import weka.filters.unsupervised.attribute.Remove;

@RestController
public class MainController {
	Method selectedMethod = null;
	int rank = 1;
	
	@RequestMapping(value = "/results", method = RequestMethod.POST)
	public ResponseDTO getResults(@RequestBody RequestDTO request)
			throws Exception {
		System.out.println(request.toString());
		clusterDoc(2, 10);
		rank =1;
		selectedMethod = null;
		ResponseDTO response = new ResponseDTO();
		CsvToBean<ServiceData> csv = new CsvToBean<ServiceData>();
		CSVReader csvReader = new CSVReader(new FileReader("./input.csv"));
		List<ServiceData> serviceDataList = csv.parse(setColumMapping(), csvReader);
		String givenQuery = request.getAttributeName();

		if (givenQuery != null) {
			ServiceData data = new ServiceData();
			selectedMethod = data.getClass().getDeclaredMethod("get" + givenQuery);
		}
		serviceDataList.remove(0);
		Collections.sort(serviceDataList, new Comparator<ServiceData>() {

			@Override
			public int compare(ServiceData arg0, ServiceData arg1) {
				// TODO Auto-generated method stub
				Float a = null, b = null;
				try {
					a = Float.valueOf((String) selectedMethod.invoke(arg0));
					b = Float.valueOf((String) selectedMethod.invoke(arg1));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return a.compareTo(b);
			}
		});
		List<ResultVO> resultVO = new ArrayList<>();

		serviceDataList.forEach(as -> {
			ResultVO result = new ResultVO();
			try {
				System.out.println(selectedMethod.invoke(as));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result.setRank(rank);
			result.setServiceName(as.getServiceName());
			result.setServiceUrl(as.getWSDLAddress());
			rank++;
			resultVO.add(result);
		});
		System.out.println("Total " + serviceDataList.size() + " Services");
		System.out.println(givenQuery);

		response.setResponseCode("1");
		response.setResponseMessage("DONE");
		response.setResult(resultVO);
		return response;
	}

	@SuppressWarnings("deprecation")
	private ColumnPositionMappingStrategy<ServiceData> setColumMapping() {
		ColumnPositionMappingStrategy<ServiceData> strategy = new ColumnPositionMappingStrategy<ServiceData>();
		strategy.setType(ServiceData.class);
		String[] columns = new String[] { "ResponseTime", "Availability", "Throughput", "Successability", "Reliability",
				"Compliance", "BestPractices", "Latency", "Documentation", "ServiceName", "WSDLAddress" };
		strategy.setColumnMapping(columns);
		return strategy;
	}
	
	public static void clusterDoc(int k, int seed) throws Exception {
		SimpleKMeans kmeans = new SimpleKMeans();
		MakeDensityBasedClusterer fc = new MakeDensityBasedClusterer();
		// important parameter to set: preserver order, number of cluster.
		kmeans.setPreserveInstancesOrder(true);
		kmeans.setNumClusters(k);

		BufferedReader datafile = readDataFile("./qws.arff");
		Instances data = new Instances(datafile);
		/*CSVSaver saver = new CSVSaver();
		saver.setInstances(data);
		saver.setFile(new File("./input.csv"));
		saver.writeBatch();*/
		String[] options = new String[2];
		options[0] = "-R"; // "range"
		options[1] = "10-11";
		System.out.println(data);
		Remove remove = new Remove(); // new instance of filter
		remove.setOptions(options); // set options
		remove.setInputFormat(data); // inform filter about dataset **AFTER** setting options
		Instances newData = Filter.useFilter(data, remove);

		System.out.println(newData);
		fc.setClusterer(kmeans);
		fc.buildClusterer(newData);

		// This array returns the cluster number (starting with 0) for each
		// instance
		// The array has as many elements as the number of instances
		int[] assignments = kmeans.getAssignments();
		AddCluster addCluster = new AddCluster();
		addCluster.setClusterer(kmeans);
		addCluster.setInputFormat(newData);
		Instances newData1 = Filter.useFilter(newData, addCluster);

		System.out.println(newData1);
		// Saving new data
		CSVSaver savers = new CSVSaver();
		savers.setInstances(newData1);
		savers.setFile(new File("./out.csv"));
		savers.writeBatch();
	/*	
		JSONSaver jsaver = new JSONSaver();
		jsaver.setInstances(newData1);
		jsaver.setFile(new File("./out.json"));
		jsaver.writeBatch();*/
		int i = 0;
		for (int clusterNum : assignments) {

			System.out.printf("Instance %d -> Cluster %d \n", i, clusterNum);
			i++;
		}
		
		/* ArffSaver save =new ArffSaver(); 
		 save.setInstances(newData1); 
		 save.setFile(new
		  File("./out1.arff")); save.setDestination(new File("./out1.arff"));
		 save.writeBatch();*/
		 

	//	BufferedWriter writer = new BufferedWriter(new FileWriter("./out.arff"));

		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(kmeans);
		eval.evaluateClusterer(data);

		System.out.println(eval.clusterResultsToString());
	}

	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;

		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}

		return inputReader;
	}
}
