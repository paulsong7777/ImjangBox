package com.imjangbox.share;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShareSnapshotMapper {

	void insertSnapshot(ShareSnapshotWriteRow row);

	void insertFacility(ShareFacilitySnapshotRow row);

	void insertImage(ShareImageSnapshotRow row);

	void insertAuditLog(ShareSnapshotAuditWriteRow row);

	int countSnapshotsByInspectionId(@Param("inspectionId") long inspectionId);

	Optional<ShareSnapshotRow> findSnapshotByShareId(String shareId);

	List<ShareFacilitySnapshotRow> findFacilitiesByShareId(String shareId);

	List<ShareImageSnapshotRow> findImagesByShareId(String shareId);

	Optional<ShareImageSnapshotRow> findImageByShareIdAndDisplayOrder(
			@Param("shareId") String shareId,
			@Param("displayOrder") int displayOrder);

	List<ShareSnapshotAuditRow> findAuditLogsByShareId(@Param("shareId") String shareId);
}
