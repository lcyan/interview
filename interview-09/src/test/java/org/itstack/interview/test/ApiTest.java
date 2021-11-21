package org.itstack.interview.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;

import org.itstack.interview.TestDelayed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.awt.util.IdentityLinkedList;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;

public class ApiTest {

	private Logger logger = LoggerFactory.getLogger(ApiTest.class);

	public static final List<String> list = new ArrayList<>();

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
		for (int i = 1; i <= 205; i++) {
			System.out.printf("开始处理第%d页\n", i);
			test_fetch(i);
		}
		File file = new File(String.format("D:\\data\\tmp\\%s.txt", DateUtil.format(new Date(), "yyyy_MM_dd")));
		FileUtil.writeLines(list, file, StandardCharsets.UTF_8);
	}

	private void test_fetch(int i) {
		HttpRequest request = new HttpRequest(String.format("https://v1.xianbao.net/forum" +
				".php?mod=viewthread&tid=25356&checkrush=1&page=%d", i));

		Map<String, String> headers = new HashMap<>();
		headers.put("x-nws-log-uuid", "4656790857529850960");
		headers.put("x-nws-uuid-verify", "c75f5a15fda7fa0d29f2fb5ce1a1e516");


		request.addHeaders(headers);

		HttpResponse response = request.execute();
		Document document = Jsoup.parse(response.body());
		Elements elements = document.getElementById("postlist").select(".plhin");
		for (Element element : elements) {

			Element name = element.select(".plc .pi a.xi2").get(0);
			Element floor = element.select(".plc .pi strong>a").get(0);
			Elements em = floor.select("em");

			String value;
			if (em.size() == 0) {
				value = floor.html().trim();
			} else {
				value = em.html().trim();
			}
			list.add(name.html() + "\t" + value);
		}
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
