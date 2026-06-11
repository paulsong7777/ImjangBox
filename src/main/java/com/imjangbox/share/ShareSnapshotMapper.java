package com.imjangbox.share;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShareSnapshotMapper {

	void insertSnapshot(ShareSnapshotWriteRow row);

	void insertFacility(ShareFacilitySnapshotRow row);

	void insertImage(ShareImageSnapshotRow row);

	Optional<ShareSnapshotRow> findSnapshotByShareId(String shareId);

	List<ShareFacilitySnapshotRow> findFacilitiesByShareId(String shareId);

	List<ShareImageSnapshotRow> findImagesByShareId(String shareId);

	Optional<ShareImageSnapshotRow> findImageByShareIdAndDisplayOrder(String shareId, int displayOrder);
}
