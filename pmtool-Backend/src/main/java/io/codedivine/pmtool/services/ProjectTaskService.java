package io.codedivine.pmtool.services;

import io.codedivine.pmtool.domain.Backlog;
import io.codedivine.pmtool.domain.Project;
import io.codedivine.pmtool.domain.ProjectTask;
import io.codedivine.pmtool.exception.ProjectNotFoundException;
import io.codedivine.pmtool.repositories.BacklogRepository;
import io.codedivine.pmtool.repositories.ProjectRepository;
import io.codedivine.pmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private  ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier,ProjectTask projectTask,String username){
            try{
                Backlog backlog=projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);-vise ne ni treba
                projectTask.setBacklog(backlog);
                Integer BacklogSequence=backlog.getPTSequence();
                BacklogSequence++;
                backlog.setPTSequence(BacklogSequence);
                projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
                projectTask.setProjectIdentifier(projectIdentifier);

                if(projectTask.getPriority()==null||projectTask.getPriority()==0 ){
                    projectTask.setPriority(3);
                }
                if(projectTask.getStatus()=="" || projectTask.getStatus()==null){
                    projectTask.setStatus("TO_DO");
                }
                return projectTaskRepository.save(projectTask);
            } catch (Exception e){
                throw new ProjectNotFoundException("Project not found!");

        }


    }

     public Iterable<ProjectTask>findBacklogById(String id,String username){
       projectService.findProjectByIdentifier(id,username);


        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
    public ProjectTask findPTByProjectSequence(String backlog_id,String pt_id,String username){

        Backlog backlog =backlogRepository.findByProjectIdentifier(backlog_id);



        projectService.findProjectByIdentifier(backlog_id,username);

        // da se osigurame dali taskot postoe prvo
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask==null){
            throw new ProjectNotFoundException("Project Task "+pt_id+" not found ");
        }
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project task '"+pt_id+"' does not exists in project'"+backlog_id);
        }
        return projectTask;
    }
    public ProjectTask updateByProjectSequence(ProjectTask updatedTask,String backlog_id,String pt_id,String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id,username);

        projectTask = updatedTask;
        return projectTaskRepository.save(projectTask);
    }
    public void deletePTByProjectSequence(String backlog_id, String pt_id,String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id,username);

        projectTaskRepository.delete(projectTask);
    }
}
