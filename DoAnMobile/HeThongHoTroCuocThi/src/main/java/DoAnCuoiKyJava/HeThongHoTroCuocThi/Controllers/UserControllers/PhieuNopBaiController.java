package DoAnCuoiKyJava.HeThongHoTroCuocThi.Controllers.UserControllers;

import DoAnCuoiKyJava.HeThongHoTroCuocThi.Entities.*;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Request.NoiDungCreateRequest;
import DoAnCuoiKyJava.HeThongHoTroCuocThi.Services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/User/PhieuNopBais")
public class PhieuNopBaiController {
    private final PhieuNopBaiService phieuNopBaiService;
    private final UserService userService;
    private final ChiTietNoiDungService CTNDService;
    private final NoiDungService noiDungService;

    @PostMapping("/add")
    public String addNoiDung(
            @Valid @ModelAttribute("PhieuNopBai") PhieuNopBai phieuNopBai,
            @RequestParam(value = "cuocThiId", required = false) Long cuocThiId,
            @RequestParam(value = "noiDungId", required = false) Long noiDungId,
            @RequestParam(value = "UrlFile", required = false) MultipartFile UrlFile,
            Principal principal,
            @NotNull BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            List<ChiTietNoiDung> chiTietNoiDungs = CTNDService.getChiTietNoiDungsByCuocThiId(cuocThiId);
            model.addAttribute("chiTietNoiDungs", chiTietNoiDungs);
            model.addAttribute("cuocThiId", cuocThiId);
            return "User/NoiDung/list";
        }

        phieuNopBai.setCuocThiId(cuocThiId);

        NoiDung noiDung = noiDungService.getNoiDungById(noiDungId).orElseThrow();
        phieuNopBai.setNoiDung(noiDung);

        User user = userService.findByUsername(principal.getName());
        phieuNopBai.setUser(user);

        phieuNopBaiService.add(phieuNopBai, UrlFile);
        return "redirect:/User/CuocThis/noi-dung/" + cuocThiId;
    }

    // c=copy controller này để chuyển đến details
    @GetMapping("/details/{id}/{cuocThiId}")
    public String showNoiDung(@PathVariable Long id,
                              @PathVariable Long cuocThiId,
                              @NotNull Model model) {
        PhieuNopBai phieuNopBai = phieuNopBaiService.findById(id).orElseThrow();
        model.addAttribute("cuocThiId",cuocThiId);
        model.addAttribute("phieuNopBai", phieuNopBai);
        return "/User/PhieuNopBai/details";
    }
}