package com.dmm.task.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class MainController {

	@Autowired
	private TasksRepository tasksRepository;

	@GetMapping("/main")
	public String getmain(Model model, @AuthenticationPrincipal AccountUserDetails user) {

		LocalDate ym = LocalDate.now();
		DateTimeFormatter sdf1 = DateTimeFormatter.ofPattern("yyyy年MM月");
		String formatNowDate = sdf1.format(ym);
		model.addAttribute("month", formatNowDate);

		List<List<LocalDate>> list = new ArrayList<>();

		// 1週間分のLocalDateを格納するListを用意する
		List<LocalDate> week = new ArrayList<>();

		// その月の1日のLocalDateを取得する
		LocalDate ld = ym.withDayOfMonth(1);

		// 曜日を表すDayOfWeekを取得し、上で取得したLocalDateに曜日の値（DayOfWeek#getValue)をマイナスして前月分のLocalDateを求める
		int w = ld.getDayOfWeek().getValue();
		if (w != 7) {
			ld = ld.plusDays(-w);
		}
		// 1日ずつ増やしてLocalDateを求めていき、2．で作成したListへ格納していき、1週間分詰めたら1．のリストへ格納する
		for (int i = 0; i < 7; i++) {
			week.add(ld);
			ld = ld.plusDays(1);
		}
		list.add(week);

		/*
		 * 2週目以降は単純に1日ずつ日を増やしながらLocalDateを求めてListへ格納していき、
		 * 土曜日になったら1．のリストへ格納して新しいListを生成する（月末を求めるにはLocalDate#lengthOfMonth()を使う）
		 */
		week = new ArrayList<>();
		int stratDay = ld.getDayOfMonth();
		int lastDay = ld.lengthOfMonth();
		for (int i = stratDay; i <= lastDay; i++) {
			DayOfWeek dw = ld.getDayOfWeek();
			week.add(ld);
			if (dw == DayOfWeek.SATURDAY) {
				list.add(week);
				week = new ArrayList<>();
			}
			ld = ld.plusDays(1);
		}

		// 最終週の翌月分をDayOfWeekの値を使って計算し、6．で生成したリストへ格納し、最後に1．で生成したリストへ格納する
		w = ld.getDayOfWeek().getValue();
		if (w != 7) {
			for (int i = w; i < 7; i++) {
				week.add(ld);
				ld = ld.plusDays(1);
			}
			list.add(week);
		}

		model.addAttribute("matrix", list);
		/*
		 * 8. 管理者は全員分のタスクを見えるようにする
		 */

		Map<LocalDate, List<Tasks>> mvm = new LinkedHashMap<>();

		String userName = user.getName();

		for (int i = 0; i < list.size(); i++) {
			List<LocalDate> l = list.get(i);
			for (int j = 0; j < l.size(); j++) {
				if (userName.equals("admin-name")) {
					mvm.put(list.get(i).get(j), tasksRepository.findAllByDateBetween(list.get(i).get(j)));
				} else {
					mvm.put(list.get(i).get(j), tasksRepository.findByDateBetween(list.get(i).get(j), userName));
				}
			}
		}

		mvm.forEach((x, y) -> {
			//System.out.println(y);
			model.addAttribute("tasks.get(day)", y);
		});

		//System.out.println(userName);
		//System.out.println(ym.withDayOfMonth(1));
		//System.out.println(ym.withDayOfMonth(ym.lengthOfMonth()));
		// System.out.println(tasksRepository.findAllByDateBetween(ym.withDayOfMonth(1),ym.withDayOfMonth(ym.lengthOfMonth())));

		return "main";
	}

}
