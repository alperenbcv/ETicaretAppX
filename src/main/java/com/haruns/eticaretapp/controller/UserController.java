package com.haruns.eticaretapp.controller;

import com.haruns.eticaretapp.dto.request.*;
import com.haruns.eticaretapp.dto.response.BaseResponse;
import com.haruns.eticaretapp.dto.response.IMyProfile;
import com.haruns.eticaretapp.entity.User;
import com.haruns.eticaretapp.exception.ErrorType;
import com.haruns.eticaretapp.exception.EticaretException;
import com.haruns.eticaretapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.haruns.eticaretapp.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserController {
	private final UserService userService;
	
	@PostMapping(REGISTER)
	public ResponseEntity<BaseResponse<Boolean>> register(@RequestBody @Valid UserRegisterRequestDto dto){
		if (!dto.password().equals(dto.rePassword())) {
			throw new EticaretException(ErrorType.PASSWORD_ERROR);
		}
		userService.register(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				                         .code(200)
				                         .success(true)
				                         .data(true)
				                         .message("Kayıt başarıyla oluşturuldu.")
		                                 .build());
	}
	
	@PostMapping(LOGIN)
	public ResponseEntity<BaseResponse<String>> doLogin(@RequestBody @Valid LoginRequestDto dto){
		String token=userService.login(dto);
		return ResponseEntity.ok(BaseResponse.<String>builder()
				                         .code(200)
				                         .message("Giriş işlemi başarılı.")
				                         .success(true)
				                         .data(token)
		                                 .build());
	}
	
	@PostMapping(STORE_REGISTER)
	public ResponseEntity<BaseResponse<Boolean>> storeRegister(@RequestBody @Valid StoreRegisterRequestDto dto){
		if (!dto.password().equals(dto.rePassword())) {
			throw new EticaretException(ErrorType.PASSWORD_ERROR);
		}
		userService.storeRegister(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Kayıt başarıyla oluşturuldu.")
		                                     .build());
	}
	
	@GetMapping(GET_MY_PROFILE)
	public ResponseEntity<BaseResponse<IMyProfile>> getMyProfile(String token){
		return ResponseEntity.ok(BaseResponse.<IMyProfile>builder()
				                         .code(200)
				                         .message("Profil başarıyla getirildi")
				                         .success(true)
				                         .data(userService.getMyProfile(token))
		                                     .build());
	}
	@PutMapping(UPDATE_MY_PROFILE)
	public ResponseEntity<BaseResponse<Boolean>> updateMyProfile(String token,
	                                                             @RequestBody @Valid UpdateMyProfileRequestDto dto){
		userService.updateMyProfile(token, dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				                         .code(200)
				                         .message("Güncelleme işlemi başarıyla tamamlandı.")
				                         .data(true)
				                         .success(true)
		                                 .build());
	}
	
	@PutMapping(UPDATE_MY_STORE_PROFILE)
	public ResponseEntity<BaseResponse<Boolean>> updateMyStoreProfile(String token,
	                                                             @RequestBody @Valid UpdateMyStoreProfileRequestDto dto){
		userService.updateMyStoreProfile(token, dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .message("Güncelleme işlemi başarıyla tamamlandı.")
		                                     .data(true)
		                                     .success(true)
		                                     .build());
	}
	
}