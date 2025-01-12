package com.nexavault.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nexavault.model.FileAccessControl;

public interface FileAccessControlDao extends JpaRepository<FileAccessControl, String> {
	boolean existsByFileIpfsHashAndUserEmailAndAccessLevel(String ipfsHash, String email,
			FileAccessControl.AccessLevel level);

	List<FileAccessControl> findByFileIpfsHash(String ipfsHash);
}