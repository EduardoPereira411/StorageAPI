# StorageAPI

This projects aims to aid in the management of any item/type of item the user desires to keep track off.

This project provides also provides a working template for a Dockerfile to easily build a Docker image for the StorageAPI application if you so wish, with an explanation on how to do so down below:

## 1. Building the Docker Image

### 1.1 Open a terminal and navigate to the root directory of this project (where the Dockerfile is located).

### 1.2. Run the following command to build the Docker image:

```sh
docker build -t storageapi:latest .
```

- `-t storageapi:latest` tags the image as `storageapi` with the `latest` tag.
- The `.` specifies the build context as the current directory.

## 2. Running the Docker Container

After building the image, you can run a container with:

```sh
docker run -d -p 8080:8080 storageapi:latest
```

- Adjust the port mapping (`-p`) as needed for your application, if needed at all (in case you are using a proxy)

## EXTRA: Pushing image to Docker Hub

If you have a docker account and wish to have version control or simply use it seamlessly on other devices, you can push this image onto docker hub with some extra steps.

First you need to login onto docker via console, inputting your credentials after you do:

```sh
docker login
```

Next, you need to make sure the image you create has the format `USERNAME\IMAGE_NAME` so that docker knows where to push the image to. Assuming you have built the image with the instructions from step [1.1](#11-open-a-terminal-and-navigate-to-the-root-directory-of-this-project-where-the-dockerfile-is-located), you can simply do:

```sh
docker tag storageapi USERNAME/storageapi:VERSION
```

Replacing **USERNAME** with your docker username and the **VERSION** with the value you desire. After that, you can push the image to docker by doing:

```sh
docker push USERNAME/storageapi:VERSION
```
