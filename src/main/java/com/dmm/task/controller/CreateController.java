package com.dmm.task.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.form.CreateForm;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class CreateController {

	@Autowired
	private TasksRepository repo;

	@GetMapping("/main/create/{date}")
	public String regist(@PathVariable(name = "date") String date, Model model) {
		CreateForm createForm = new CreateForm();
		model.addAttribute("createForm", createForm);
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate d = LocalDate.parse(date, f);
		model.addAttribute("date", d);

		return "/create";
	}

	@PostMapping("/main/create")
	public String create(@Validated CreateForm createForm, BindingResult bindingResult,
			@AuthenticationPrincipal AccountUserDetails user, Model model) {

		if (bindingResult.hasErrors()) {
			List<Tasks> list = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("posts", list);
			model.addAttribute("CreateForm", createForm);

			return "/create";
		}

		Tasks task = new Tasks();
		task.setName(user.getName());
		task.setTitle(createForm.getTitle());
		task.setText(createForm.getText());
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate d = LocalDate.parse(createForm.getDate(), f);
		task.setDate(d);
		task.setDone(false);

		repo.save(task);

		return "redirect:/main";
	}

}
