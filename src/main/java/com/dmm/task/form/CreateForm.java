package com.dmm.task.form;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CreateForm {
	// titleへのバリデーション設定を追加
	@Size(min = 1, max = 200)
	private String title;
	// textへのバリデーション設定を追加
	@Size(min = 1, max = 200)
	private String text;

	@Size(min = 1, max = 200)
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private String date;

	@AssertTrue
	private Boolean done;

}