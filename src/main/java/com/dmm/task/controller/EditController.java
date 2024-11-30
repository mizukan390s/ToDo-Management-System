package com.dmm.task.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

@Controller
public class EditController {

	@Autowired
	private TasksRepository repo;

	@GetMapping("/main/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		// 今選択したタスクが欲しい。
		Tasks task = repo.getById(id);

		model.addAttribute("task", task);

		return "/edit";
	}

	@PostMapping("/main/edit/{id}")
	public String update(@Validated CreateForm createForm, BindingResult bindingResult, Model model,
			@PathVariable Long id) {

		if (bindingResult.hasErrors()) {
			List<Tasks> list = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("posts", list);
			model.addAttribute("CreateForm", createForm);

			return edit(id, model);
		}

		Tasks task = repo.getById(id);
		task.setTitle(createForm.getTitle());
		task.setText(createForm.getText());
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate d = LocalDate.parse(createForm.getDate(), f);
		task.setDate(d);
		if (createForm.getDone() == null) {
			task.setDone(false);
		} else {
			task.setDone(createForm.getDone());
		}

		repo.save(task);

		return "redirect:/main";
	}

	@PostMapping("/main/delete/{id}")
	public String delete(@PathVariable Long id) {
		repo.deleteById(id);
		return "redirect:/main";
	}

}
