// This file is for internal AMD use.
// If you are interested in running your own Jenkins, please raise a github issue for assistance.

def runCompileCommand(platform, project, jobName)
{
    project.paths.construct_build_prefix()

    String hipclangArgs = jobName.contains('hipclang') ? '--hip-clang' : ''
    def command = """#!/usr/bin/env bash
                set -x
                cd ${project.paths.project_build_prefix}
                LD_LIBRARY_PATH=/opt/rocm/hcc/lib CXX= ${project.paths.build_command} -t ${hipclangArgs}
            """

    platform.runCommand(this,command)
}


def runPackageCommand(platform, project, jobName)
{
    def command = """
                      set -x
                      cd ${project.paths.project_build_prefix}/build/release
                      make package
                      rm -rf package && mkdir -p package
                      mv *.deb package/
                      """
	platform.runCommand(this,command)
    platform.archiveArtifacts(this, """${project.paths.project_build_prefix}/build/release/package/*.deb""")
}

return this
