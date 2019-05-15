package com.dianhang.oa.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dianhang.common.annotation.Log;
import com.dianhang.common.base.AjaxResult;
import com.dianhang.common.enums.BusinessType;
import com.dianhang.core.config.Global;
import com.dianhang.framework.web.BaseController;
import com.dianhang.framework.web.TableDataInfo;
import com.dianhang.oa.domain.Contract;
import com.dianhang.oa.domain.ContractFile;
import com.dianhang.oa.domain.FlowProcess;
import com.dianhang.oa.domain.ProjectContract;
import com.dianhang.oa.service.IContractService;
import com.dianhang.shiro.util.ShiroUtils;
import com.dianhang.system.domain.SysUser;
import com.dianhang.system.service.ISysUserService;
import com.dianhang.util.FileUploadUtils;

@Controller
@RequestMapping("/oa/contract")
public class ContractController extends BaseController {

	private String prefix = "oa/contract";
	
	private String contractFilePath = Global.getProfile() + "contract/";

	@Autowired
	private IContractService contractService;

	@Autowired
	private ISysUserService sysUserService;

	/**
	 * 新增工程合同
	 * 
	 * @return
	 */
	@RequiresPermissions("contract:project:add")
	@GetMapping("/project")
	public String addProjectContract(ModelMap mmap) {

		SysUser sysUser = ShiroUtils.getSysUser();

		ProjectContract projectContract = new ProjectContract();
		projectContract.setApplyDate(new Date());
		mmap.put("projectContract", projectContract);

		// 查找拥有“区域经理”角色的用户
		List<SysUser> areaManagerList = sysUserService.selectUserListByRole("area_manager");
		mmap.put("areaManagerList", areaManagerList);

		return prefix + "/project";
	}

	/**
	 * 保存工程合同
	 * 
	 * @throws Exception
	 */
	@RequiresPermissions("contract:project:add")
	@Log(title = "工程合同", businessType = BusinessType.INSERT)
	@PostMapping("/project")
	@ResponseBody
	public AjaxResult saveProjectContract(HttpServletRequest request, OAForm form,
			@RequestParam(name = "contractfile", required = false) MultipartFile[] fileList) throws Exception {
//		Map<String, String[]> a = request.getParameterMap();
//		for (Map.Entry<String, String[]> entry : a.entrySet()) {
//			System.err.println(entry.getKey());
//			System.err.println(Arrays.toString(entry.getValue()));
//		}
		SysUser sysUser = ShiroUtils.getSysUser();

		// 抄送人员
		String users = form.getInformUserList();
		Map<String, String> userMap = new LinkedHashMap<String, String>();
		if (StringUtils.isNotBlank(users)) {
			for (String user : users.split(",")) {
				String[] arr = user.split("-");
				userMap.put(arr[0], arr[1]);
			}
		}

		// 附件
		Map<String, String> fileMap = new LinkedHashMap<String, String>();
		if (null != fileList) {
			for (MultipartFile file : fileList) {
				String fileName = file.getOriginalFilename();
				System.err.println(fileName);
				String avatar = FileUploadUtils.upload(contractFilePath, file,
						fileName.substring(fileName.lastIndexOf(".")));
				System.err.println(avatar);
				fileMap.put(avatar, fileName);
			}
		}

//		if (true) {
//			return AjaxResult.error();
//		}
		return toAjax(contractService.saveProjectContract(sysUser, form.getProjectContract(),
				form.getContractProductList(), userMap, fileMap));
	}

	/**
	 * 新增特价合同
	 * 
	 * @return
	 */
	@RequiresPermissions("contract:special:add")
	@GetMapping("/special")
	public String addSppecialContract() {
		return prefix + "/special";
	}

	/**
	 * 保存特价合同
	 * 
	 * @throws Exception
	 */
	@RequiresPermissions("contract:special:add")
	@Log(title = "特价合同", businessType = BusinessType.INSERT)
	@PostMapping("/special")
	@Transactional(rollbackFor = Exception.class)
	@ResponseBody
	public AjaxResult saveSppecialContract(OAForm form) throws Exception {
		SysUser sysUser = ShiroUtils.getSysUser();
		return toAjax(contractService.saveSppecialContract(sysUser, form.getSpecialContract(),
				form.getContractProductList()));
	}

	/**
	 * 审核合同信息
	 */
	@GetMapping("/deal/{contractId}")
	public String deal(@PathVariable("contractId") String contractId, ModelMap mmap) {
		String[] arr = contractId.split("_");
		mmap.put("contract", contractService.getContract(arr[0], arr[1]));
		mmap.put("contractProductList", contractService.queryContractProductList(arr[0]));
		mmap.put("contractFileList", contractService.queryContractFileList(arr[0]));
		List<FlowProcess> list = contractService.queryFlowProcessList(arr[0]);

		String currentActivity = null;
		for (FlowProcess p : list) {
			if ("0".equals(p.getStatus())) {
				currentActivity = p.getActivity();
				break;
			}
		}
		mmap.put("flowProcessList", list);
		mmap.put("currentActivity", currentActivity);
		
		if("1".equals(currentActivity)) {
			// 查找拥有“区域经理”角色的用户
			List<SysUser> areaManagerList = sysUserService.selectUserListByRole("area_manager");
			mmap.put("areaManagerList", areaManagerList);
		}
		
		return prefix + "/deal" + arr[1];
	}

	@RequiresPermissions("contract:project:add")
	@Log(title = "工程合同", businessType = BusinessType.INSERT)
	@PostMapping("/deal")
	@ResponseBody
	public AjaxResult deal(HttpServletRequest request, OAForm form,
			@RequestParam(name = "contractfile", required = false) MultipartFile[] fileList) throws Exception {

		SysUser sysUser = ShiroUtils.getSysUser();

		// 抄送人员
		String users = form.getInformUserList();
		Map<String, String> userMap = new LinkedHashMap<String, String>();
		if (StringUtils.isNotBlank(users)) {
			for (String user : users.split(",")) {
				String[] arr = user.split("-");
				userMap.put(arr[0], arr[1]);
			}
		}

		// 附件
		Map<String, String> fileMap = new LinkedHashMap<String, String>();
		if (null != fileList) {
			for (MultipartFile file : fileList) {
				String fileName = file.getOriginalFilename();
				System.err.println(fileName);
				String avatar = FileUploadUtils.upload(contractFilePath, file,
						fileName.substring(fileName.lastIndexOf(".")));
				System.err.println(avatar);
				fileMap.put(avatar, fileName);
			}
		}

		return toAjax(contractService.saveProjectContract(sysUser, form.getProjectContract(),
				form.getContractProductList(), userMap, fileMap, form.getFlowProcess()));
	}

	/**
	 * 查看合同信息
	 */
	@GetMapping("/detail/{contractId}")
	public String detail(@PathVariable("contractId") String contractId, ModelMap mmap) {
		String[] arr = contractId.split("_");
		mmap.put("contract", contractService.getContract(arr[0], arr[1]));
		mmap.put("contractProductList", contractService.queryContractProductList(arr[0]));
		mmap.put("contractFileList", contractService.queryContractFileList(arr[0]));
		mmap.put("flowProcessList", contractService.queryFlowProcessList(arr[0]));
		return prefix + "/detail" + arr[1];
	}

	/**
	 * 新增特价合同
	 * 
	 * @return
	 */
	@RequiresPermissions("contract:approve")
	@GetMapping("/approve")
	public String approve() {
		return prefix + "/approve";
	}

	@RequiresPermissions("contract:approve")
	@PostMapping("/approve")
	@ResponseBody
	public TableDataInfo approve(Contract contract) {
		if (null == contract) {
			contract = new Contract();
		}
		SysUser sysUser = ShiroUtils.getSysUser();
		contract.setRelationUserId(sysUser.getUserId());
		contract.setStatus("0");
		startPage();
		List<Contract> list = contractService.selectContractList(contract);
		return getDataTable(list);
	}

	/**
	 * 
	 * @return
	 */
	@RequiresPermissions("contract:inform")
	@GetMapping("/inform")
	public String inform() {
		return prefix + "/inform";
	}

	@RequiresPermissions("contract:inform")
	@PostMapping("/inform")
	@ResponseBody
	public TableDataInfo inform(Contract contract) {
		if (null == contract) {
			contract = new Contract();
		}
		SysUser sysUser = ShiroUtils.getSysUser();
		contract.setRelationUserId(sysUser.getUserId());
		contract.setStatus("9");
		startPage();
		List<Contract> list = contractService.selectContractList(contract);
		return getDataTable(list);
	}

	/**
	 * 
	 * @return
	 */
	@RequiresPermissions("contract:statistics")
	@GetMapping("/statistics")
	public String statistics() {
		return prefix + "/statistics";
	}

	@RequiresPermissions("contract:statistics")
	@PostMapping("/statistics")
	@ResponseBody
	public TableDataInfo statistics(Contract contract) {
		if (null == contract) {
			contract = new Contract();
		}
		SysUser sysUser = ShiroUtils.getSysUser();
		contract.setRelationUserId(sysUser.getUserId());
		startPage();
		List<Contract> list = contractService.selectContractList(contract);
		return getDataTable(list);
	}
	
	@RequestMapping("/downloadContractFile/{id}")
	public void downloadFileAction(@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding(request.getCharacterEncoding());
		response.setContentType("application/octet-stream");
		FileInputStream fis = null;
		try {
			ContractFile contractFile = contractService.getContractFile(id);
			File file = new File(contractFilePath, contractFile.getFileName());
			fis = new FileInputStream(file);
			response.setHeader("Content-Disposition", "attachment; filename=" + contractFile.getOriginalFileName());
			IOUtils.copy(fis, response.getOutputStream());
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
