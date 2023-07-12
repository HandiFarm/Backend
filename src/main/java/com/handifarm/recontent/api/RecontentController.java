package com.handifarm.recontent.api;


import com.handifarm.recontent.dto.page.RecontentPageDTO;
import com.handifarm.recontent.dto.request.RecontentCreateRequestDTO;
import com.handifarm.recontent.dto.response.RecontentListResponseDTO;
import com.handifarm.recontent.entity.Recontent;
import com.handifarm.recontent.service.RecontentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/recontent")
public class RecontentController {

    private final RecontentService recontentService;

    //댓글 목록
    @GetMapping
    public ResponseEntity<?> retrieveList(Recontent cboard , RecontentPageDTO pageDTO){

        RecontentListResponseDTO dto = recontentService.contentretrieve(cboard , pageDTO);

        return ResponseEntity.ok().body(dto);
    }

    //댓글 등록 요청
    @PostMapping
    public ResponseEntity<?> createRecontent(
            @Validated @RequestBody RecontentCreateRequestDTO dto,
            BindingResult result
    ){

        log.info("/api/recontent/post -{}",dto);

        if(dto == null) {
            return ResponseEntity.badRequest().body("댓글 등록 제목을 전달해 주세요.");
        }

        ResponseEntity<List<FieldError>> fieldErrors = getValidated(result);

        if(fieldErrors != null){
            return fieldErrors;
        }

        try {

            RecontentListResponseDTO recontentListResponseDTO = recontentService.create(dto);
            return ResponseEntity.ok().body(recontentListResponseDTO);
        } catch (IllegalStateException e){
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("서버 오류 발생: " + e.getMessage());
        } catch (Exception e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private static ResponseEntity<List<FieldError>> getValidated(BindingResult result) {
        if(result.hasErrors()){

            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(dm -> {
                log.warn("invalid client {} " , dm.toString());
            });

            return ResponseEntity.badRequest().body(fieldErrors);
        }

        return null;

    }


}
