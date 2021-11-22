package org.itstack.interview.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.itstack.interview.TestDelayed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;

public class ApiTest {

	private Logger logger = LoggerFactory.getLogger(ApiTest.class);

	public static final List<String> DETAIL_LIST = new ArrayList<>();
	public static final Map<String, Integer> NAME_COUNT = new HashMap<>();

	@Test
	public void test_BlockingQueue() {
		Queue<String> queue = new LinkedBlockingQueue<String>();
	}

	@Test
	public void test_Queue() throws InterruptedException {
		Queue<String> queue00 = new LinkedList<String>();
		queue00.poll();
		queue00.offer("");

		Queue<String> queue01 = new LinkedBlockingQueue<String>();
		queue01.offer("");
		queue01.poll();

		Queue<String> queue02 = new ArrayBlockingQueue<String>(10);

	}

	@Test
	public void test_Deque_LinkedList() {
		Deque<String> deque = new LinkedList<>();
		deque.push("a");
		deque.push("b");
		deque.push("c");
		deque.push("d");

		deque.offerLast("e");
		deque.offerLast("f");
		deque.offerLast("g");
		deque.offerLast("h");

		deque.push("i");
		deque.offerLast("j");

		System.out.println("数据出栈：");
		while (!deque.isEmpty()) {
			System.out.print(deque.pop() + " ");
		}

	}

	@Test
	public void test_DelayQueue() throws InterruptedException {
		DelayQueue<TestDelayed> delayQueue = new DelayQueue<TestDelayed>();
		delayQueue.offer(new TestDelayed("aaa", 5, TimeUnit.SECONDS));
		delayQueue.offer(new TestDelayed("ccc", 1, TimeUnit.SECONDS));
		delayQueue.offer(new TestDelayed("bbb", 3, TimeUnit.SECONDS));

		logger.info(((TestDelayed) delayQueue.take()).getStr());
		logger.info(((TestDelayed) delayQueue.take()).getStr());
		logger.info(((TestDelayed) delayQueue.take()).getStr());
	}

	@Test
	public void test_deque() {
		Deque<String> deque00 = new ArrayDeque<String>(10);
		deque00.offer("");
		deque00.poll();

		deque00.push("");

		Deque<String> deque01 = new LinkedList<String>();

		Deque<String> deque02 = new LinkedBlockingDeque<String>();

		Deque<String> deque04 = new ConcurrentLinkedDeque<String>();

	}

	@Test
	public void test_ArrayDeque2() {
		Deque<String> deque = new ArrayDeque<String>(1);

		deque.push("a");
		deque.push("b");
		deque.push("c");
		deque.push("d");

		deque.push("e");
		deque.push("f");

		deque.offerLast("g");
		deque.offerLast("h");
		deque.offerLast("i");
		deque.offerLast("j");

		System.out.println(deque.pop());
		System.out.println(deque.pop());
		System.out.println(deque.pop());
		System.out.println(deque.pop());

		System.out.println(deque.pollLast());

	}


	@Test
	public void test_ArrayDeque() {
		Deque<String> deque = new ArrayDeque<String>(1);

		deque.push("a");
		deque.push("b");
		deque.push("c");
		deque.push("d");

		deque.offerLast("e");
		deque.offerLast("f");
		deque.offerLast("g");
		deque.offerLast("h");  // 这时候扩容了

		deque.push("i");
		deque.offerLast("j");

		System.out.println("数据出栈：");
		while (!deque.isEmpty()) {
			System.out.print(deque.pop() + " ");
		}

	}

	@Test
	public void testStatistic() {
		DETAIL_LIST.clear();
		NAME_COUNT.clear();
		for (int i = 1; i <= 1; i++) {
			System.out.printf("开始处理第%d页\n", i);
			try {
				test_fetch(i);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String dateStr = DateUtil.format(new Date(), "yyyy_MM_dd");
		File fileDetail = new File(String.format("D:\\data\\tmp\\%s_detail.txt", dateStr));
		File fileStatistic = new File(String.format("D:\\data\\tmp\\%s_statistics.txt", dateStr));
		FileUtil.writeLines(DETAIL_LIST, fileDetail, StandardCharsets.UTF_8);

		// 将map.entrySet()转换成list
		List<Map.Entry<String, Integer>> list = new ArrayList<>(NAME_COUNT.entrySet());
		// 通过比较器来实现排序
		list.sort((o1, o2) -> {
			// 降序排序
			return o2.getValue().compareTo(o1.getValue());
		});
		for (Map.Entry<String, Integer> mapping : list) {
			FileUtil.writeString(mapping.getKey() + "\t" + mapping.getValue(), fileStatistic, StandardCharsets.UTF_8);
		}
	}

	private void test_fetch(int i) throws IOException {
		String tid = "25356";
		String url = String.format("https://v1.xianbao.net/forum.php?mod=viewthread&tid=%s&checkrush=1&page=%d", tid,
				i);

		HttpRequest request = new HttpRequest(url);
		request.addHeaders(makeHeaders(tid));
		HttpResponse response = request.execute();

		Document document = Jsoup.parse(response.body());

		Elements elements = document.select("#postlist .plhin");
		for (Element element : elements) {

			Element nameEle = element.select(".plc .pi a.xi2").first();
			Element floorEle = element.select(".plc .pi strong>a").first();

			assert nameEle != null;
			assert floorEle != null;

			Elements em = floorEle.select("em");

			String value;
			if (em.size() == 0) {
				value = floorEle.text().trim();
			} else {
				value = em.text().trim();
			}
			DETAIL_LIST.add(nameEle.text() + "\t" + value);
			NAME_COUNT.merge(nameEle.text(), 1, Integer::sum);
		}
	}

	private Map<String, String> makeHeaders(String tid) {
		Map<String, String> headers = new HashMap<>();
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
				"Chrome/95.0.4638.69 Safari/537.36");
		headers.put("x-nws-log-uuid", "4656790857529850960");
		headers.put("x-nws-uuid-verify", "c75f5a15fda7fa0d29f2fb5ce1a1e516");
		headers.put("referer", String.format("https://v1.xianbao.net/forum.php?mod=viewthread&tid=%s&checkrush=1",
				tid));
		return headers;
	}

	@Test
	public void test_arraycopy() {
		int head = 0, tail = 0;

		Object[] elements = new Object[8];
		elements[head = (head - 1) & (elements.length - 1)] = "a";
		elements[head = (head - 1) & (elements.length - 1)] = "b";
		elements[head = (head - 1) & (elements.length - 1)] = "c";
		elements[head = (head - 1) & (elements.length - 1)] = "d";

		elements[tail] = "e";
		tail = (tail + 1) & (elements.length - 1);
		elements[tail] = "f";
		tail = (tail + 1) & (elements.length - 1);
		elements[tail] = "g";
		tail = (tail + 1) & (elements.length - 1);
		elements[tail] = "h";
		tail = (tail + 1) & (elements.length - 1);

		System.out.println("head：" + head);
		System.out.println("tail：" + tail);

		int p = head;
		int n = elements.length;
		int r = n - p; // number of elements to the right of p

		System.out.println(JSON.toJSONString(elements));
		// head == tail 扩容
		Object[] a = new Object[8 << 1];
		System.arraycopy(elements, p, a, 0, r);
		System.out.println(JSON.toJSONString(a));
		System.arraycopy(elements, 0, a, r, p);
		System.out.println(JSON.toJSONString(a));
		elements = a;
		head = 0;
		tail = n;

		a[head = (head - 1) & (a.length - 1)] = "i";
		elements[tail] = "j";
		tail = (tail + 1) & (elements.length - 1);

		System.out.println(JSON.toJSONString(a));
	}

	@Test
	public void test_stack() {
		Stack<String> s = new Stack<String>();

		s.push("aaa");
		s.push("bbb");
		s.push("ccc");

		System.out.println("获取最后一个元素：" + s.peek());
		System.out.println("获取最后一个元素：" + s.lastElement());
		System.out.println("获取最先放置元素：" + s.firstElement());

		System.out.println("弹出一个元素[LIFO]：" + s.pop());
		System.out.println("弹出一个元素[LIFO]：" + s.pop());
		System.out.println("弹出一个元素[LIFO]：" + s.pop());

		/**
		 * 获取最后一个元素：ccc
		 * 获取最后一个元素：ccc
		 * 获取最先放置元素：aaa
		 * 弹出一个元素[LIFO]：ccc
		 * 弹出一个元素[LIFO]：bbb
		 * 弹出一个元素[LIFO]：aaa
		 */
	}

}
