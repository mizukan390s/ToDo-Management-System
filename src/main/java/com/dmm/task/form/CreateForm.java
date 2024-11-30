package com.dmm.task.form;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CreateForm {
	// titleへのバリデーション設定を追加
	@Size(min = 1, max = 200)
	private String title;
	// textへのバリデーション設定を追加
	@Size(min = 1, max = 200)
	private String text;
	
	@JsonFormat(pattern = "YYYY-MM-dd")
	private String date;
	
}