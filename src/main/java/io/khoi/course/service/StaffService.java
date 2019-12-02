package io.khoi.course.service;

import io.khoi.course.model.Staff;
import io.khoi.course.repository.StaffRepository;
import io.khoi.course.repository.UnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for staff-* commands.
 */
@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    private static final Logger LOG
            = LoggerFactory.getLogger(StaffService.class);

    /**
     * Get all staffs
     * @return
     */
    public List<Staff> getStaffs() {
        return staffRepository.findAll();
    }

    /**
     * Get staff by id
     * @param id
     * @return the optional staff
     */
    public Optional<Staff> getStaff(Long id) {
        return staffRepository.findById(id);
    }

    /**
     * Seed the data from staffs.txt if there is no staff in the db.
     * @throws IOException
     */
    @PostConstruct
    public void seedData() throws IOException {
        if (staffRepository.count() > 0) {
            LOG.info("skipping staffs.txt import");
            return;
        }

        Resource resource = new ClassPathResource("staffs.txt");
        InputStream input = resource.getInputStream();
        File file = resource.getFile();
        BufferedReader br = new BufferedReader(new FileReader(file));

        int n = Integer.parseInt(br.readLine());

        for (int i = 0; i < n; ++i) {
            String[] idAndName = br.readLine().split(",");
            if (idAndName.length < 2) { // malformed, bail
                LOG.error("staffs.txt malformed, skipping");
            }
            String address = br.readLine();
            Staff s = new Staff();
            s.setStaffId(Long.parseLong(idAndName[0]));
            s.setFullName(idAndName[1]);
            s.setAddress(address);
            staffRepository.save(s);
        }
        LOG.info("staffs.txt imported");
    }
}
