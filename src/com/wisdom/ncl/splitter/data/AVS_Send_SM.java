package com.wisdom.ncl.splitter.data;

public class AVS_Send_SM {

	private static final long serialVersionUID = 1L;

	public AVS_Send_SM() {
	}

	public AVS_Send_SM(String provinceId, int sendId, String sendName,
			String sendType, String sendDescription, String smcontent,
			String releaseTime, String sendTargetType, String sendTarget,
			int sendTargetRatio, int sendTargetNumber, String groupName,
			int sendGroupRatio, int sendGroupNumber, int priority,
			String sendImmediate, String requestBranchId, String requestUserId,
			String rsendTimeBegin, String rsendTimeEnd, int rcompleteHourBegin,
			int rcompleteHourEnd, String applied, String applyTime,
			String checked, String checkTime, String checkNote,
			String promptToApplier, String promptToApplierTime,
			String finishTime, int sendTargetCount, int roadUsed,
			int abortFlag, int promptBySm, String serviceNo,String tab_name) {
		province_id = provinceId;
		send_id = sendId;
		send_name = sendName;
		send_type = sendType;
		send_description = sendDescription;
		this.smcontent = smcontent;
		release_time = releaseTime;
		send_target_type = sendTargetType;
		send_target = sendTarget;
		send_target_ratio = sendTargetRatio;
		send_target_number = sendTargetNumber;
		group_name = groupName;
		send_group_ratio = sendGroupRatio;
		send_group_number = sendGroupNumber;
		this.priority = priority;
		send_immediate = sendImmediate;
		request_branch_id = requestBranchId;
		request_user_id = requestUserId;
		rsend_time_begin = rsendTimeBegin;
		rsend_time_end = rsendTimeEnd;
		rcomplete_hour_begin = rcompleteHourBegin;
		rcomplete_hour_end = rcompleteHourEnd;
		this.applied = applied;
		apply_time = applyTime;
		this.checked = checked;
		check_time = checkTime;
		check_note = checkNote;
		prompt_to_applier = promptToApplier;
		prompt_to_applier_time = promptToApplierTime;
		finish_time = finishTime;
		send_target_count = sendTargetCount;
		road_used = roadUsed;
		abort_flag = abortFlag;
		prompt_by_sm = promptBySm;
		service_no = serviceNo;
		this.tab_name = tab_name;
	}

	private String province_id;
	private int send_id;
	private String send_name;
	private String send_type;
	private String send_description;
	private String smcontent;
	private String release_time;
	private String send_target_type;
	private String send_target;
	private int send_target_ratio;
	private int send_target_number;
	private String group_name;
	private int send_group_ratio;
	private int send_group_number;
	private int priority;
	private String send_immediate;
	private String request_branch_id;
	private String request_user_id;
	private String rsend_time_begin;
	private String rsend_time_end;
	private int rcomplete_hour_begin;
	private int rcomplete_hour_end;
	private String applied;
	private String apply_time;
	private String checked;
	private String check_time;
	private String check_note;
	private String prompt_to_applier;
	private String prompt_to_applier_time;
	private String finish_time;
	private int send_target_count;
	private int road_used;
	private int abort_flag;
	private int prompt_by_sm;
	private String service_no;
	private String tab_name;

	public String getProvince_id() {

		return province_id;
	}

	public void setProvince_id(String provinceId) {

		province_id = provinceId;
	}

	public int getSend_id() {

		return send_id;
	}

	public void setSend_id(int sendId) {

		send_id = sendId;
	}

	public String getSend_name() {

		return send_name;
	}

	public void setSend_name(String sendName) {

		send_name = sendName;
	}

	public String getSend_type() {

		return send_type;
	}

	public void setSend_type(String sendType) {

		send_type = sendType;
	}

	public String getSend_description() {

		return send_description;
	}

	public void setSend_description(String sendDescription) {

		send_description = sendDescription;
	}

	public String getSmcontent() {

		return smcontent;
	}

	public void setSmcontent(String smcontent) {

		this.smcontent = smcontent;
	}

	public String getRelease_time() {

		return release_time;
	}

	public void setRelease_time(String releaseTime) {

		release_time = releaseTime;
	}

	public String getSend_target_type() {

		return send_target_type;
	}

	public void setSend_target_type(String sendTargetType) {

		send_target_type = sendTargetType;
	}

	public String getSend_target() {

		return send_target;
	}

	public void setSend_target(String sendTarget) {

		send_target = sendTarget;
	}

	public int getSend_target_ratio() {

		return send_target_ratio;
	}

	public void setSend_target_ratio(int sendTargetRatio) {

		send_target_ratio = sendTargetRatio;
	}

	public int getSend_target_number() {

		return send_target_number;
	}

	public void setSend_target_number(int sendTargetNumber) {

		send_target_number = sendTargetNumber;
	}

	public String getGroup_name() {

		return group_name;
	}

	public void setGroup_name(String groupName) {

		group_name = groupName;
	}

	public int getSend_group_ratio() {

		return send_group_ratio;
	}

	public void setSend_group_ratio(int sendGroupRatio) {

		send_group_ratio = sendGroupRatio;
	}

	public int getSend_group_number() {

		return send_group_number;
	}

	public void setSend_group_number(int sendGroupNumber) {

		send_group_number = sendGroupNumber;
	}

	public int getPriority() {

		return priority;
	}

	public void setPriority(int priority) {

		this.priority = priority;
	}

	public String getSend_immediate() {

		return send_immediate;
	}

	public void setSend_immediate(String sendImmediate) {

		send_immediate = sendImmediate;
	}

	public String getRequest_branch_id() {

		return request_branch_id;
	}

	public void setRequest_branch_id(String requestBranchId) {

		request_branch_id = requestBranchId;
	}

	public String getRequest_user_id() {

		return request_user_id;
	}

	public void setRequest_user_id(String requestUserId) {

		request_user_id = requestUserId;
	}

	public String getRsend_time_begin() {

		return rsend_time_begin;
	}

	public void setRsend_time_begin(String rsendTimeBegin) {

		rsend_time_begin = rsendTimeBegin;
	}

	public String getRsend_time_end() {

		return rsend_time_end;
	}

	public void setRsend_time_end(String rsendTimeEnd) {

		rsend_time_end = rsendTimeEnd;
	}

	public int getRcomplete_hour_begin() {

		return rcomplete_hour_begin;
	}

	public void setRcomplete_hour_begin(int rcompleteHourBegin) {

		rcomplete_hour_begin = rcompleteHourBegin;
	}

	public int getRcomplete_hour_end() {

		return rcomplete_hour_end;
	}

	public void setRcomplete_hour_end(int rcompleteHourEnd) {

		rcomplete_hour_end = rcompleteHourEnd;
	}

	public String getApplied() {

		return applied;
	}

	public void setApplied(String applied) {

		this.applied = applied;
	}

	public String getApply_time() {

		return apply_time;
	}

	public void setApply_time(String applyTime) {

		apply_time = applyTime;
	}

	public String getChecked() {

		return checked;
	}

	public void setChecked(String checked) {

		this.checked = checked;
	}

	public String getCheck_time() {

		return check_time;
	}

	public void setCheck_time(String checkTime) {

		check_time = checkTime;
	}

	public String getCheck_note() {

		return check_note;
	}

	public void setCheck_note(String checkNote) {

		check_note = checkNote;
	}

	public String getPrompt_to_applier() {

		return prompt_to_applier;
	}

	public void setPrompt_to_applier(String promptToApplier) {

		prompt_to_applier = promptToApplier;
	}

	public String getPrompt_to_applier_time() {

		return prompt_to_applier_time;
	}

	public void setPrompt_to_applier_time(String promptToApplierTime) {

		prompt_to_applier_time = promptToApplierTime;
	}

	public String getFinish_time() {

		return finish_time;
	}

	public void setFinish_time(String finishTime) {

		finish_time = finishTime;
	}

	public int getSend_target_count() {

		return send_target_count;
	}

	public void setSend_target_count(int sendTargetCount) {

		send_target_count = sendTargetCount;
	}

	public int getRoad_used() {

		return road_used;
	}

	public void setRoad_used(int roadUsed) {

		road_used = roadUsed;
	}

	public int getAbort_flag() {

		return abort_flag;
	}

	public void setAbort_flag(int abortFlag) {

		abort_flag = abortFlag;
	}

	public int getPrompt_by_sm() {

		return prompt_by_sm;
	}

	public void setPrompt_by_sm(int promptBySm) {

		prompt_by_sm = promptBySm;
	}

	public String getService_no() {

		return service_no;
	}

	public void setService_no(String serviceNo) {

		service_no = serviceNo;
	}

	public void setTab_name(String tab_name) {
		
		this.tab_name = tab_name;
		
	}

	public String getTab_name() {
		
		return tab_name;
	}

}