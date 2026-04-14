package com.freelancedetector.service;

import com.freelancedetector.dto.ProjectDTO;
import com.freelancedetector.entity.Client;
import com.freelancedetector.entity.Project;
import com.freelancedetector.entity.User;
import com.freelancedetector.repository.ClientRepository;
import com.freelancedetector.repository.ProjectRepository;
import com.freelancedetector.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    public ProjectDTO addProject(ProjectDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Project project = new Project();
        project.setClient(client);
        project.setUser(user);
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setProjectType(dto.getProjectType());
        project.setAgreedAmount(dto.getAgreedAmount());
        project.setAgreedHours(dto.getAgreedHours());
        project.setDeadline(dto.getDeadline());
        project.setStatus(dto.getStatus());

        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    public List<ProjectDTO> getProjectsByUser(Long userId) {
        return projectRepository.findByUserUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return convertToDTO(project);
    }

    public ProjectDTO updateProjectStatus(Long id, String status) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setStatus(status);
        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(project.getProjectId());
        dto.setClientId(project.getClient() != null ? project.getClient().getClientId() : null);
        dto.setUserId(project.getUser() != null ? project.getUser().getUserId() : null);
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setProjectType(project.getProjectType());
        dto.setAgreedAmount(project.getAgreedAmount());
        dto.setAgreedHours(project.getAgreedHours());
        dto.setDeadline(project.getDeadline());
        dto.setStatus(project.getStatus());
        return dto;
    }
}
