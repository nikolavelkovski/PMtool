package io.codedivine.pmtool.services;

import io.codedivine.pmtool.domain.Backlog;
import io.codedivine.pmtool.domain.Project;
import io.codedivine.pmtool.domain.User;
import io.codedivine.pmtool.exception.ProjectIdException;
import io.codedivine.pmtool.exception.ProjectNotFoundException;
import io.codedivine.pmtool.repositories.BacklogRepository;
import io.codedivine.pmtool.repositories.ProjectRepository;
import io.codedivine.pmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){
        //koga kreirame nov project project.getId==null
        //koga updatirame project.getid!=null
        //najdi project u db id ->null
        if(project.getId() !=null) {
            Project existingProject = projectRepository.findProjectByProjectIdentifier(project.getProjectIdentifier());
            if(existingProject != null &&(!existingProject.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Project not found in your account!!!");

            } else if(existingProject == null){
                throw new ProjectNotFoundException("Project with id:'"+project.getProjectIdentifier()+" ' cannot be updated because it's doesn ' t exist!!");
            }
        }
        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());

            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if(project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
           if(project.getId()!=null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
           }


            return projectRepository.save(project);

        } catch(Exception e){
            throw new ProjectIdException("Project ID'" + project.getProjectIdentifier().toUpperCase()+"'already exists!");
        }

    }
    public Project findProjectByIdentifier(String projectId, String username){

        Project project = projectRepository.findProjectByProjectIdentifier(projectId.toUpperCase());

        if(project ==null){
            throw new ProjectIdException("Project ID "+projectId+" does not exists!");

        }
        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account ");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(String username){

        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId,String username){

        projectRepository.delete(findProjectByIdentifier(projectId,username));

    }

}
